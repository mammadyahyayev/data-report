package az.my.datareport.scrape;

import az.my.datareport.ast.DataAST;
import az.my.datareport.ast.DataElement;
import az.my.datareport.ast.DataNode;
import az.my.datareport.model.ReportData;
import az.my.datareport.model.ReportDataElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WebScraper implements Scraper {

    private static final Logger LOG = LoggerFactory.getLogger(WebScraper.class);

    @Override
    public List<ReportData> scrape(DataAST dataAST) {
        Objects.requireNonNull(dataAST);
        List<ReportData> reportDataList = new ArrayList<>();

        List<DataNode> dataNodes = dataAST.getDataNodes();
        for (DataNode dataNode : dataNodes) {
            WebPage page = new WebPage(dataNode.getUrl());
            page.connect();

            List<ReportDataElement> elements = fetchDataFromUrl(page, dataNode.getElements());

            ReportData reportData = new ReportData();
            reportData.setUrl(dataNode.getUrl());
            reportData.setReportDataElements(elements);
            reportDataList.add(reportData);
        }

        return reportDataList;
    }

    private List<ReportDataElement> fetchDataFromUrl(final WebPage page, final List<DataElement> elements) {
        List<ReportDataElement> reportDataElements = new ArrayList<>();
        elements.forEach(element -> {
            List<String> values = page.fetchElements(element.getSelector());
            ReportDataElement reportData = new ReportDataElement(element.getName(), values);
            reportDataElements.add(reportData);
        });
        return reportDataElements;
    }
}
