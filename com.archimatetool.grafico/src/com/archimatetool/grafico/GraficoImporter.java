package com.archimatetool.grafico;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.model.*;
// Импортируем класс Messages из правильного пакета
import com.archimatetool.grafico.Messages;

public class GraficoImporter {
    private IArchimateModel fModel;
    private Map<String, IIdentifier> idLookup;
    private MultiStatus resolveErrors;
    private ResourceSet resourceSet;

    public GraficoImporter(IArchimateModel model) {
        fModel = model;
    }

    public IArchimateModel doImport(File folder) throws IOException {
        if (folder == null) return fModel;
        File modelFolder = new File(folder, GraficoExporter.MODEL_FOLDER);
        File imagesFolder = new File(folder, GraficoExporter.IMAGES_FOLDER);
        if (!modelFolder.isDirectory()) return fModel;

        resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMLResourceFactoryImpl());
        idLookup = new HashMap<>();
        loadModel(modelFolder);
        resourceSet.getResource(URI.createFileURI(new File(modelFolder, GraficoExporter.FOLDER_XML).getAbsolutePath()), true).getContents().remove(fModel);
        resolveProxies(fModel);

        if (imagesFolder.isDirectory()) loadImages(fModel, imagesFolder);
        if (resolveErrors != null)
            ErrorDialog.openError(Display.getCurrent().getActiveShell(), Messages.GraficoImporter_1, Messages.GraficoImporter_2, resolveErrors);
        return fModel;
    }

    private void loadImages(IArchimateModel fModel, File folder) throws IOException {
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(fModel);
        for (File imageFile : folder.listFiles()) {
            if (imageFile.isFile()) {
                byte[] bytes = Files.readAllBytes(imageFile.toPath());
                archiveManager.addByteContentEntry("images/" + imageFile.getName(), bytes);
            }
        }
    }

    private void resolveProxies(EObject object) {
        for (Iterator<EObject> iter = object.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if (eObject instanceof IArchimateRelationship) {
                IArchimateRelationship relation = (IArchimateRelationship) eObject;
                relation.setSource((IArchimateConcept) resolve(relation.getSource(), relation));
                relation.setTarget((IArchimateConcept) resolve(relation.getTarget(), relation));
            } else if (eObject instanceof IDiagramModelArchimateObject) {
                IDiagramModelArchimateObject element = (IDiagramModelArchimateObject) eObject;
                element.setArchimateElement((IArchimateElement) resolve(element.getArchimateElement(), element));
                element.getArchimateElement().getReferencingDiagramObjects().add(element);
            } else if (eObject instanceof IDiagramModelArchimateConnection) {
                IDiagramModelArchimateConnection archiConnection = (IDiagramModelArchimateConnection) eObject;
                archiConnection.setArchimateRelationship((IArchimateRelationship) resolve(archiConnection.getArchimateRelationship(), archiConnection));
                archiConnection.getArchimateRelationship().getReferencingDiagramConnections().add(archiConnection);
            } else if (eObject instanceof IDiagramModelReference) {
                IDiagramModelReference element = (IDiagramModelReference) eObject;
                element.setReferencedModel((IDiagramModel) resolve(element.getReferencedModel(), element));
            }
        }
    }

    private EObject resolve(IIdentifier object, IIdentifier parent) {
        if (object != null && object.eIsProxy()) {
            IIdentifier newObject = idLookup.get(((InternalEObject) object).eProxyURI().fragment());
            if (newObject == null) {
                String message = String.format(Messages.GraficoImporter_3, ((InternalEObject) object).eProxyURI().fragment(), parent.getClass().getSimpleName(), parent.getId());
                System.out.println(message);
                if (resolveErrors == null)
                    resolveErrors = new MultiStatus("com.archimatetool.grafico", IStatus.ERROR, Messages.GraficoImporter_4, null);
                resolveErrors.add(new Status(IStatus.ERROR, "com.archimatetool.grafico", message));
            }
            return newObject == null ? object : newObject;
        } else {
            return object;
        }
    }

    private IArchimateModel loadModel(File folder) {
        fModel = (IArchimateModel) loadElement(new File(folder, GraficoExporter.FOLDER_XML));
        if (fModel != null) {
            for (FolderType folderType : FolderType.values()) {
                IFolder tmpFolder = loadFolder(new File(folder, folderType.toString()));
                if (tmpFolder != null) {
                    fModel.getFolders().add(tmpFolder);
                }
            }
        }
        return fModel;
    }

    private IFolder loadFolder(File folder) {
        if (!folder.isDirectory() || !(new File(folder, GraficoExporter.FOLDER_XML)).isFile()) return null;
        IFolder currentFolder = (IFolder) loadElement(new File(folder, GraficoExporter.FOLDER_XML));
        for (File fileOrFolder : folder.listFiles()) {
            if (!fileOrFolder.getName().equals(GraficoExporter.FOLDER_XML)) {
                if (fileOrFolder.isFile()) {
                    currentFolder.getElements().add(loadElement(fileOrFolder));
                } else {
                    currentFolder.getFolders().add(loadFolder(fileOrFolder));
                }
            }
        }
        return currentFolder;
    }

    private EObject loadElement(File file) {
        XMLResource resource = (XMLResource) resourceSet.getResource(URI.createFileURI(file.getAbsolutePath()), true);
        resource.getDefaultLoadOptions().put(XMLResource.OPTION_ENCODING, "UTF-8");
        IIdentifier element = (IIdentifier) resource.getContents().get(0);
        idLookup.put(element.getId(), element);
        return element;
    }
}