<#import "parts/common.ftl" as c>

<@c.page>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-12">
                <ul class="nav nav-pills">
                    <li class="nav-item">
                        <a class="nav-link" href="byDoctor">По доктору</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="bySpeciality">По специализации</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div class="container pt-2">
        <div class="row justify-content-center">
            <div class="col d-flex">
                <div class="card">
                    <div class="card-header">Выберите специализацию</div>
                    <div class="card-body">
                        <table class="table">
                            <tbody>
                            <#list specialities as s>
                                <tr>
                                    <td> ${s}</td>
                                    <td>
                                        <form name="my-form" method="get" action="${requestcontext.contextPath}/doctorsBySpeciality">
                                            <div>
                                                <input type="hidden" name="speciality" value="${s}">
                                                <button type="submit" class="btn btn-primary btn-sm">
                                                    Список
                                                </button>
                                            </div>
                                        </form>
                                    </td>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@c.page>