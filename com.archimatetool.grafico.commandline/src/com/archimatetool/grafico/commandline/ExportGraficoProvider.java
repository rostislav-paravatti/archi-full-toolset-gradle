package com.archimatetool.grafico.commandline;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.commandline.AbstractCommandLineProvider;
import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.grafico.GraficoExporter; // Убедитесь, что этот класс существует
import com.archimatetool.grafico.commandline.Messages; // Убедитесь, что этот класс и пакет существуют

public class ExportGraficoProvider extends AbstractCommandLineProvider {

    static final String PREFIX = Messages.ExportGraficoProvider_0;
    
    static final String OPTION_EXPORT_GRAFICO = "grafico.export"; //$NON-NLS-1$

    public ExportGraficoProvider() {
    }
    
    @Override
    public void run(CommandLine commandLine) throws Exception {
        if (!hasCorrectOptions(commandLine)) {
            return;
        }
        
        IArchimateModel model = CommandLineState.getModel();
        
        if (model == null) {
            throw new IOException(Messages.ExportGraficoProvider_1);
        }
        GraficoExporter exporter = new GraficoExporter(model);
        
        // Folder
        String grafico_export_path = commandLine.getOptionValue(OPTION_EXPORT_GRAFICO);
        if (!StringUtils.isSet(grafico_export_path)) {
            logError(Messages.ExportGraficoProvider_2);
            return;
        }
        File folderOutput = new File(grafico_export_path);
        folderOutput.mkdirs();
        if (!folderOutput.exists()) {
            logError(NLS.bind(Messages.ExportGraficoProvider_3, grafico_export_path));
            return;
        }
        
        exporter.export(folderOutput);
        logMessage(Messages.ExportGraficoProvider_5);
    }
    
    @Override
    protected String getLogPrefix() {
        return PREFIX;
    }
    
    @Override
    public Options getOptions() {
        Options options = new Options();
        
        Option option = Option.builder()
                .longOpt(OPTION_EXPORT_GRAFICO)
                .hasArg().argName(Messages.ExportGraficoProvider_6)
                .desc(Messages.ExportGraficoProvider_7)
                .build();
        options.addOption(option);
        
        return options;
    }
    
    private boolean hasCorrectOptions(CommandLine commandLine) {
        return commandLine.hasOption(OPTION_EXPORT_GRAFICO);
    }
}