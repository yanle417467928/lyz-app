<aside class="main-sidebar">
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="/images/user2-160x160.jpg" class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
                <p>Alexander Pierce</p>
                <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
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
        <ul class="sidebar-menu">
            <li class="header">操作菜单</li>
            <#if menuVOList?? && menuVOList?size gt 0>
                <#list menuVOList as item>
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
                </#list>
            </#if>
        </ul>
    </section>
</aside>