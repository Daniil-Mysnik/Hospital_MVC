<#import "parts/common.ftl" as c>

<@c.page>
    <div class="container pt-2">
        <div class="d-flex row justify-content-center">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">Выберите талон</div>
                    <div class="card-body">
                        <div class="table-responsive col-md-12 text-break">
                            <form name="my-form" method="get"
                                  action="${requestcontext.contextPath}/doctorTickets/${doctor.getId()}">
                                <div>
                                    <label>Вывести талоны</label>
                                    <#if dateStart??>
                                        <label for="dateStart">от:</label>
                                        <input type="date" name="dateStart" id="dateStart" value="${dateStart}">
                                        <label for="dateEnd">До:</label>
                                        <input type="date" name="dateEnd" id="dateEnd" value="${dateEnd}">
                                    <#else>
                                        <label for="dateStart">от:</label>
                                        <input type="date" name="dateStart" id="dateStart">
                                        <label for="dateEnd">До:</label>
                                        <input type="date" name="dateEnd" id="dateEnd">
                                    </#if>
                                    <button type="submit" class="btn btn-primary btn-sm m-2 p-1 py-0">
                                        Выбрать
                                    </button>
                                </div>
                            </form>
                            <#if doctor.getDayScheduleList()?size == 0>
                            <label>Талонов на данный промежуток времени нет</label>
                            <#else>
                            <#list doctor.getDayScheduleList() as dsl>
                            <table class="table">

                                <thead>
                                <#if dsl.getAppointments()?size != 0>
                                <label>${dsl.getDate()}</label>
                                </#if>
                                </thead>
                                <tbody>
                                <tr class="row justify-content-center">
                                    <#list dsl.getAppointments() as aps>
                                        <td class="text-center">
                                            <#if aps.getDaySchedule().checkDate(.now?date)>
                                                <a href="login" class="btn btn-warning mt-1 disabled"
                                                   role="button">${aps.getTime()}</a>
                                            <#else>
                                                <#if aps.getState().toString() == "FREE">
                                                    <a href="login" class="btn btn-success mt-1"
                                                       role="button">${aps.getTime()}</a>
                                                <#else>
                                                    <a href="login" class="btn btn-danger mt-1 disabled"
                                                       role="button">${aps.getTime()}</a>
                                                </#if>
                                            </#if>
                                        </td>
                                    </#list>
                                </tr>
                                </#list>
                                </#if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</@c.page>