package cyl;

import com.acme.biz.api.i18n.PropertySourceMessageSource;
import org.junit.Test;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author chenyilei
 * @date 2023/04/18 20:15
 */
public class _5_国际化 {

    @Test
    public void locale_本地化索引() {

        Locale aDefault = Locale.getDefault();

        System.err.println(aDefault);

        /**
         * resource => 资源
         */
    }


    /**
     * @see PropertySourceMessageSource
     *
     * {@link PropertyResourceBundle}
     * {@link  com.acme.biz.api.i18n.HardCodeResourceBundle} 硬编码类实现 国际化文案 , 可用于热部署
     */
    @Test
    public void ResourceBundle_class_properties两种支持() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("META-INF/Messages", Locale.ENGLISH);
        System.err.println(resourceBundle.getLocale());
        System.err.println(resourceBundle.getString("my.name"));
    }
}
