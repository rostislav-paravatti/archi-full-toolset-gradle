package com.archimatetool.grafico;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.IModelExporter;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IFolderContainer;
// Импортируем класс Messages из правильного пакета
import com.archimatetool.grafico.Messages;

public class GraficoExporter {

    private IArchimateModel fModel;
    private ResourceSet resourceSet;

    static final String FOLDER_XML = "folder.xml";
    static final String IMAGES_FOLDER = "images";
    static final String MODEL_FOLDER = "model";
    
    public GraficoExporter(IArchimateModel model) {
        fModel = model;
    }

    public void export(File folder) throws IOException {
        if(folder == null) {
            return;
        }

        // Setup target directories
        File modelFolder = new File(folder, MODEL_FOLDER);
        FileUtils.deleteFolder(modelFolder);
        modelFolder.mkdirs();

        File imagesFolder = new File(folder, IMAGES_FOLDER);
        FileUtils.deleteFolder(imagesFolder);
        imagesFolder.mkdirs();

        // Save model images
        saveImages(fModel, imagesFolder.getParentFile());
        
        // Initialize ResourceSet
        resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMLResourceFactoryImpl());
        resourceSet.setURIConverter(new ExtensibleURIConverterImpl());
        
        // Work on a copy of the model
        IArchimateModel copy = EcoreUtil.copy(fModel);
        createAndSaveResourceForFolder(copy, modelFolder);
        
        // Save resources
        for (Resource resource : resourceSet.getResources()) {
            resource.save(null);
        }
    }
    
    private void createAndSaveResourceForFolder(IFolderContainer folderContainer, File folder) throws IOException {
        // Save child folders
        List<IFolder> allFolders = new ArrayList<>(folderContainer.getFolders());
        for (IFolder tmpFolder : allFolders) {
            File tmpFolderFile = new File(folder, getNameFor(tmpFolder));
            tmpFolderFile.mkdirs();
            createAndSaveResource(new File(tmpFolderFile, FOLDER_XML), tmpFolder);
            createAndSaveResourceForFolder(tmpFolder, tmpFolderFile);
        }        
        // Save child elements
        if (folderContainer instanceof IFolder) {
            List<EObject> allElements = new ArrayList<>(((IFolder) folderContainer).getElements());
            for (EObject tmpElement : allElements) {
                createAndSaveResource(
                        new File(folder, tmpElement.getClass().getSimpleName() + "_" + ((IIdentifier) tmpElement).getId() + ".xml"),
                        tmpElement);
            }
        }
        if (folderContainer instanceof IArchimateModel) {
            createAndSaveResource(new File(folder, FOLDER_XML), folderContainer);
        }
    }

    private String getNameFor(IFolder folder) {
        return folder.getType() == FolderType.USER ? folder.getId().toString() : folder.getType().toString();
    }

    private void createAndSaveResource(File file, EObject object) throws IOException {
        // Map the logical name (filename) to the physical name (path+filename)
        URI key = file.getName().equals(FOLDER_XML) ? URI.createFileURI(file.getAbsolutePath()) : URI.createFileURI(file.getName());
        URI value = URI.createFileURI(file.getAbsolutePath());
        resourceSet.getURIConverter().getURIMap().put(key, value);

        // Create a new resource for the file and add the object to it
        XMLResource resource = (XMLResource) resourceSet.createResource(key);
        resource.getDefaultSaveOptions().put(XMLResource.OPTION_ENCODING, "UTF-8");
        resource.getDefaultSaveOptions().put(XMLResource.OPTION_DECLARE_XML, Boolean.FALSE);
        resource.getDefaultSaveOptions().put(XMLResource.OPTION_FORMATTED, Boolean.TRUE);
        resource.getDefaultSaveOptions().put(XMLResource.OPTION_LINE_WIDTH, 5);
        resource.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.FALSE);
        resource.getDefaultSaveOptions().put(XMLResource.OPTION_CONFIGURATION_CACHE, Boolean.TRUE);
        resource.getContents().add(object);
    }

    private void saveImages(IArchimateModel fModel, File folder) throws IOException {
        List<String> added = new ArrayList<>();
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(fModel);
        
        for (Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if (eObject instanceof IDiagramModelImageProvider) {
                IDiagramModelImageProvider imageProvider = (IDiagramModelImageProvider) eObject;
                String imagePath = imageProvider.getImagePath();
                if (imagePath != null && !added.contains(imagePath)) {
                    byte[] bytes = archiveManager.getBytesFromEntry(imagePath);
                    Files.write(Paths.get(folder.getAbsolutePath() + File.separator + imagePath), bytes, StandardOpenOption.CREATE);
                    added.add(imagePath);
                }
            }
        }
    }
}