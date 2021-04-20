<#import "parts/common.ftl" as c>

<@c.page>
    <div class="container pt-2">
        <div class="row justify-content-center">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">Выберите талон</div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <form name="my-form" method="get"
                                  action="${requestcontext.contextPath}/doctorTickets/${doctor.getId()}">
                                <div>
                                    <#if dateStart??>
                                        <label for="dateStart">
                                            От
                                        </label><input type="date" name="dateStart" id="dateStart" value="${dateStart}">
                                        <label for="dateEnd">
                                            До
                                        </label><input type="date" name="dateEnd" id="dateEnd" value="${dateEnd}">
                                    <#else>
                                        <label for="dateStart">От</label>
                                        <input type="date" name="dateStart" id="dateStart">
                                        <label for="dateEnd">До</label>
                                        <input type="date" name="dateEnd" id="dateEnd">
                                    </#if>

                                    <button type="submit" class="btn btn-primary">
                                        Выбрать
                                    </button>
                                </div>
                            </form>
                            <#list doctor.getDayScheduleList() as dsl>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th scope="col" class="text-center">${dsl.getDate()}</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <#list dsl.getAppointments() as aps>
                                        <td class="text-center">
                                            <#if aps.getState().toString() == "FREE">
                                                <a href="login" class="btn btn-success mt-1"
                                                   role="button">${aps.getTime()}</a>
                                            <#else>
                                                <a href="login" class="btn btn-danger mt-1"
                                                   role="button">${aps.getTime()}</a>
                                            </#if>
                                        </td>
                                    </#list>
                                </tr>
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