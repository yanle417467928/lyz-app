<aside class="main-sidebar" >
    <section class="sidebar">
        <!-- Sidememberuser panel -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="/images/user2-160x160.jpg" class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
                <p>开发人员</p>
                <a href="#">
                    <i class="fa fa-send-o"></i>
                    开发部
                </a>
            </div>
        </div>
        <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
                <input type="text" name="q" class="form-control" placeholder="Search...">
                <span class="input-group-btn">
                    <button type="submit" name="search" id="search-btn" class="btn btn-flat">
                        <i class="fa fa-search"></i>
                    </button>
                </span>
            </div>
        </form>
        <div class="pre-scrollable" style="max-height: 800px">
        <ul class="sidebar-menu" >
            <li class="header">操作菜单</li>
        <#if IndexMenuVOList?? && IndexMenuVOList?size gt 0>
            <#list IndexMenuVOList as item>
                <li class="treeview <#if parentMenuId?? && parentMenuId?c == item.id?c>active</#if>">
                    <a href="${item.url!'#'}?menuId=${item.id?c}">
                        <i class="${item.icon!''}"></i>
                        <span>${item.resourceName!'加载中...'}</span>
                        <span class="pull-right-container">
                                    <i class="fa fa-angle-left pull-right"></i>
                                </span>
                    </a>
                    <#if item.children?? && item.children?size gt 0>
                        <ul class="treeview-menu">
                            <#list item.children as child>
                                <li>
                                    <a href="${child.url!'#'}?menuId=${child.id?c}&parentMenuId=${item.id?c}">
                                        <i class="${child.icon!'fa fa-circle-o'}"></i>
                                    ${child.resourceName!'加载中...'}
                                    </a>
                                </li>
                            </#list>
                        </ul>
                    </#if>
                </li>
            </#list>

        <#--<#list IndexMenuVOList as item>
            <#if selectedMenu?? && selectedMenu.parent.id == item.id>
            <li class="treeview menu-open active ">
                <a href="${item.linkUr!'#'}?menuId=${item.id?c}">
                    <i class="${item.iconStyle!''}"></i>
                    <span>${item.title!'加载中...'}</span>
                        <span class="pull-right-container">
                            <i class="fa fa-angle-left pull-right"></i>
                        </span>
                </a>
                <#if item.children?? && item.children?size gt 0>

                        <ul class="treeview-menu">
                            <#list item.children as child>
                                <#if selectedMenu?? && selectedMenu.id == child.id>
                                <li class="active">
                                    <a href="${child.linkUri!'#'}?menuId=${child.id?c}">
                                        <i class="${child.iconStyle!'fa fa-circle-o'}"></i>
                                            ${child.title!'加载中...'}
                                    </a>
                                </li>
                                <#else>
                                    <li>
                                        <a href="${child.linkUri!'#'}?menuId=${child.id?c}">
                                            <i class="${child.iconStyle!'fa fa-circle-o'}"></i>
                                        ${child.title!'加载中...'}
                                        </a>
                                    </li>
                                </#if>
                            </#list>
                        </ul>
                </#if>
            </li>
            <#else>
                <li class="treeview ">
                    <a href="${item.linkUr!'#'}?menuId=${item.id?c}">
                        <i class="${item.iconStyle!''}"></i>
                        <span>${item.title!'加载中...'}</span>
                        <span class="pull-right-container">
                            <i class="fa fa-angle-left pull-right"></i>
                        </span>
                    </a>
                    <#if item.children?? && item.children?size gt 0>
                        <#if selectedMenu?? && selectedMenu.id == item.id>
                            <ul class="treeview-menu">
                                <#list item.children as child>
                                    <li>
                                        <a href="${child.linkUri!'#'}?menuId=${child.id?c}">
                                            <i class="${child.iconStyle!'fa fa-circle-o'}"></i>
                                        ${child.title!'加载中...'}
                                        </a>
                                    </li>
                                </#list>
                            </ul>
                        <#else>
                            <ul class="treeview-menu">
                                <#list item.children as child>
                                    <li>
                                        <a href="${child.linkUri!'#'}?menuId=${child.id?c}">
                                            <i class="${child.iconStyle!'fa fa-circle-o'}"></i>
                                        ${child.title!'加载中...'}
                                        </a>
                                    </li>
                                </#list>
                            </ul>
                        </#if>
                    </#if>
                </li>
            </#if>
        </#list>-->
        </#if>
        </ul>
        </div>
    </section>
</aside>