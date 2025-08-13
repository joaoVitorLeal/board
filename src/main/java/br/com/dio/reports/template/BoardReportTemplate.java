package br.com.dio.reports.template;

import br.com.dio.reports.dto.BoardReportContentDTO;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;

public abstract class BoardReportTemplate {

    protected static final String REPORT_TITLE = "Relatório do Board";
    protected static final String SECTION_CARD_DATA = "Dados do Cartão";
    protected static final String SECTION_TASK_TIMES = "Tempo por Tarefa";
    protected static final String SECTION_BLOCKS = "Bloqueios e Justificativa";
    protected static final String SECTION_UNBLOCKS = "Desbloqueios e Justificativa";
    protected static final PDType1Font FONT_BODY_TEXT = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    protected static final PDType1Font FONT_TITLE_AND_SECTION = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);


    public abstract void generate(BoardReportContentDTO contentToFile) throws IOException;
}
