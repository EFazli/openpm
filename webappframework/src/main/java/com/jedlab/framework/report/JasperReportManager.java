package com.jedlab.framework.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.context.MessageSource;
import org.springframework.ui.jasperreports.JasperReportsUtils;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * @author omidp
 *
 */
public class JasperReportManager
{

    private static final String REPORT_PATH = "/report" + File.separator;

    public static File getReportFile(String fileName) throws URISyntaxException
    {
        String path = REPORT_PATH + fileName;
        URL rsrc = Thread.currentThread().getContextClassLoader().getResource(path);
        if (rsrc == null) rsrc = JasperReportManager.class.getClassLoader().getResource(path);
        if (rsrc == null) rsrc = JasperReportManager.class.getResource(path);
        if (rsrc == null) throw new IllegalArgumentException("file not found " + path);
        return Paths.get(rsrc.toURI()).toFile();
    }

    public static void exportPdf(File file, OutputStream os, JasperPaginationHandler paginationHandler) throws JRException
    {
        exportPdf(file, os, paginationHandler, new HashMap<String, Object>());
    }

    public static void exportPdf(File file, OutputStream os, JasperPaginationHandler paginationHandler, Map<String, Object> parameters)
            throws JRException
    {
        try
        {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
            JRPropertiesUtil props = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance());
            if (parameters == null) parameters = new HashMap<>();
            parameters.put("REPORT_LOCALE", new Locale("fa", "IR"));
            parameters.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle("i18n/messages", new Locale("fa", "IR")));
            JasperReportsUtils.renderAsPdf(jasperReport, parameters, new JasperPagingDatasource(paginationHandler), os);
        }
        finally
        {

        }
    }

    public static void exportExcel(File file, OutputStream os, JasperPaginationHandler paginationHandler) throws JRException
    {
        try
        {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
            JRPropertiesUtil props = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance());
            JasperReportsUtils.renderAsXls(jasperReport, new HashMap<String, Object>(), new JasperPagingDatasource(paginationHandler), os);
        }
        finally
        {

        }
    }

   
    public static class Paging
    {
        private int firstResult;
        private int maxResult;

        public Paging(int firstResult, int maxResult)
        {
            this.firstResult = firstResult;
            this.maxResult = maxResult;
        }

        public Paging getPage()
        {
            int from = ((firstResult) * maxResult);
            int to = (firstResult * maxResult) + maxResult;
            return new Paging(from, to);
        }

        public int getFirstResult()
        {
            return firstResult;
        }

        public int getMaxResult()
        {
            return maxResult;
        }

    }



}