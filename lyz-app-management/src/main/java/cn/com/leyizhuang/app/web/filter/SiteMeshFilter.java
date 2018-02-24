package cn.com.leyizhuang.app.web.filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

/**
 * @author YanLe
 * Created on 2017/5/6.
 */
public class SiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.addDecoratorPath("/*", "/sitemash/template")
                .addExcludedPath("/sitemash/template")
                .addExcludedPath("/login")
                .addExcludedPath("/druid/**")
                .addExcludedPath("/qrcode/register/**");
    }
}
