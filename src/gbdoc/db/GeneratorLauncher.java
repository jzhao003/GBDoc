package gbdoc.db;

import gubo.jdbc.mapping.TableClassGenerator;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

// 生成数据库表对应的java类。
public class GeneratorLauncher {
    public static void main(String args[]) throws SQLException, IOException {
        DataSource ds = DBUtils.getDataSource();
        TableClassGenerator generator = new TableClassGenerator();
        // Timestamp time = new Timestamp(57600000L);
        // String classSource = generator.generateSource(ds, "sifang-zhifu",
        // "c2c_alipay_account",
        // "onionpay.db",
        // "C2cAlipayAccount");
        //
        // System.out.println(classSource);

        generator.generateAndWrite(ds, "gb-doc", "standard", "src", "gbdoc.db",
                "Standard");

        generator.generateAndWrite(ds, "gb-doc", "user", "src", "gbdoc.db",
                "User");

        generator.generateAndWrite(ds, "gb-doc", "document", "src", "gbdoc.db",
                "Document");

        generator.generateAndWrite(ds, "gb-doc", "documents_set", "src",
                "gbdoc.db", "DocumentsSet");

        generator.generateAndWrite(ds, "gb-doc", "standard_section", "src",
                "gbdoc.db", "StandardSection");

        generator.generateAndWrite(ds, "gb-doc", "doc_template", "src",
                "gbdoc.db", "DocTemplate");

    }
}
