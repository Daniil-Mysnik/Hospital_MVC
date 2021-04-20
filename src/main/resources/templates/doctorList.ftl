<#import "parts/common.ftl" as c>
<#import  "parts/doctorTickets.ftl" as t>

<@c.page>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-12">
                <ul class="nav nav-pills">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="byDoctor">По доктору</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="bySpeciality">По специализации</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="container pt-2">
        <div class="row justify-content-center">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">Выберите доктора</div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th scope="col" class="text-center">Имя</th>
                                    <th scope="col" class="text-center">Фамилия</th>
                                    <th scope="col" class="text-center">Отчество</th>
                                    <th scope="col" class="text-center">Специальность</th>
                                    <th scope="col" class="text-center">Специальность</th>
                                    <th scope="col" class="text-center">Свободных талонов</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list doctors as d>
                                    <tr>
                                        <td class="text-center"> ${d.getFirstName()}</td>
                                        <td class="text-center"> ${d.getLastName()}</td>
                                        <td class="text-center"> ${d.getLastName()}</td>
                                        <td class="text-center"> ${d.getPatronymic()}</td>
                                        <td class="text-center"> ${d.getSpeciality()}</td>
                                        <td class="text-center"> ${d.getFreeTickets()}</td>
                                        <td>
<#--                                            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"-->
<#--                                                    data-target="#myModal-${d.getId()}">-->
<#--                                                -->
<#--                                            </button>-->
                                            <form name="my-form" method="get" action="${requestcontext.contextPath}/doctorTickets/${d.getId()}">
                                                <button type="submit" class="btn btn-primary btn-sm" aria-pressed="true">
                                                    Талоны
                                                </button>
                                            </form>
<#--                                            <a href="${requestcontext.contextPath}/doctorTickets?id=${d.getId()}" class="btn btn-primary btn-sm" role="button" aria-pressed="true">Талоны</a>-->
                                        </td>
                                    </tr>
                                    <@t.page doctor=d></@t.page>
                                </#list>

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@c.page>