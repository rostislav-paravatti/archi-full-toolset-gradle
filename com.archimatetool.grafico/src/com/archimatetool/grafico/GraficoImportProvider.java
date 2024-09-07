package com.archimatetool.grafico;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.jface.preference.IPreferenceStore;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.ISelectedModelImporter;
import com.archimatetool.editor.ArchiPlugin;  // Используем новый доступ к предпочтениям
import com.archimatetool.model.IArchimateModel;

// Импортируем класс Messages из правильного пакета
import com.archimatetool.grafico.Messages;

public class GraficoImportProvider implements ISelectedModelImporter {

    @Override
    public void doImport(IArchimateModel model) throws IOException {
        GraficoImporter importer = new GraficoImporter(model);
        File folder = askOpenFolder();
        model = importer.doImport(folder);
        // Open the Model in the Editor
        IEditorModelManager.INSTANCE.openModel(model);
    }

    /**
     * Ask user to select a folder.
     */
    private File askOpenFolder() {
        DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
        
        // Используем новый доступ к предпочтениям
        IPreferenceStore store = ArchiPlugin.PREFERENCES;
        
        // Set default path from preference
        dialog.setFilterPath(store.getString(GraficoExportProvider.PREF_LAST_FOLDER));
        dialog.setText(Messages.GraficoExporter_0);
        dialog.setMessage(Messages.GraficoImporter_0);
        String path = dialog.open();
        
        if(path == null) {
            return null;
        }
        
        // Save chosen path in preference
        store.setValue(GraficoExportProvider.PREF_LAST_FOLDER, path);
        
        return new File(path);
    }
}