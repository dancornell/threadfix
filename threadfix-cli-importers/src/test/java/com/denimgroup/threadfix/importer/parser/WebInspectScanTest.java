package com.denimgroup.threadfix.importer.parser;

import com.denimgroup.threadfix.importer.ScanLocationManager;
import com.denimgroup.threadfix.importer.TransactionalTest;
import com.denimgroup.threadfix.importer.utils.ScanComparisonUtils;
import org.junit.Test;
import static com.denimgroup.threadfix.importer.TestConstants.*;
/**
 * Created by denimgroup on 2/10/14.
 */
public class WebInspectScanTest extends TransactionalTest {

    public final static String[][] webInspectResults = new String [][] {
            {XSS, "Critical", "/demo/EvalInjection2.php", "command"},
            {XSS, "Critical", "/demo/XSS-cookie.php", "cookie"},
            {XSS, "Critical", "/demo/XSS-reflected2.php", "username"},
            {OS_INJECTION, "Critical", "/demo/OSCommandInjection2.php", "fileName"},
            {INFORMATION_EXPOSURE, "Critical", "/demo/SQLI2.php", "username"},
            {INFORMATION_EXPOSURE, "Critical", "/demo/password.txt", ""},
            {INFORMATION_EXPOSURE, "High", "/demo/OSCommandInjection2.php", "fileName"},
            {INFORMATION_EXPOSURE, "High", "/demo/PredictableResource.php.BAK", ""},
            {INFORMATION_EXPOSURE, "High", "/demo/PredictableResource.php.bak", ""},
            {FORCED_BROWSING, "Medium", "/test.php", ""},
            {ACCESS_CONTROL, "Medium", "/demo/XPathInjection2.php", ""},
            {LDAP_INJECTION, "Medium", "/demo/LDAPInjection2.php", ""},
            {INFORMATION_EXPOSURE, "Medium", "/demo/LDAPInjection2.php", ""},
            {INFORMATION_EXPOSURE, "Low", "/cgi-bin/test.php", ""},
            {INFORMATION_EXPOSURE, "Low", "/demo/EvalInjection2.php", ""},
            {INFORMATION_EXPOSURE, "Low", "/demo/FormatString2.php", ""},
            {INFORMATION_EXPOSURE, "Low", "/demo/OSCommandInjection2.php", ""},
            {INFORMATION_EXPOSURE, "Low", "/demo/PathTraversal.php", ""},
            {INFORMATION_EXPOSURE, "Low", "/demo/PathTraversal.php", "action"},
            {INFORMATION_EXPOSURE, "Low", "/demo/SQLI2.php", ""},
            {INFORMATION_EXPOSURE, "Low", "/demo/XPathInjection2.php", ""},
            {INFORMATION_EXPOSURE, "Low", "/demo/XSS-cookie.php", "cookie"},
            {INFORMATION_EXPOSURE, "Low", "/demo/XSS-reflected2.php", ""},
            {INFORMATION_EXPOSURE, "Low", "/test.php", ""},
            {DIRECTORY_LISTING, "Low", "/cgi-bin/", ""},
            {DIRECTORY_LISTING, "Low", "/demo/", ""},
            {INFORMATION_EXPOSURE, "Info", "/", ""},
    };

    @Test
    public void webInspectScanTest() {
        ScanComparisonUtils.compare(webInspectResults, ScanLocationManager.getRoot() +
                "Dynamic/WebInspect/webinspect-demo-site.xml");
    }
}
