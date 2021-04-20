<#macro page doctor>
    <script>
        function sendDoctor() {
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8888/hospital/byDoctor',
                data: $('#daate').val(),
                dataType: 'data'
            }).done(function(data) {
                alert(data);
            }).fail(function(err){
                alert(err);
            });
        }
    </script>
    <div class="modal fade" id="myModal-${doctor.getId()}">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Выберите время</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>Свободные талоны</p>
                    <div class="container pt-2">
                        <div class="d-flex row justify-content-center">
                            <div class="col-md-8">


<#--                                <a href="login" class="btn btn-danger mt-1" role="button">${doctor.getId()}</a>-->
                                <#--                                <a href="login" class="btn btn-danger mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-danger mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-danger mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-danger mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-danger mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-danger mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-danger mt-1" role="button">11:00</a>-->

                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                <a href="login" class="btn btn-success mt-1" role="button">11:00</a>-->
                                <#--                                ${.now?date?iso_utc}-->

                                <form name="my-form" method="get" action="${requestcontext.contextPath}/byDoctor">
                                    <div>
                                        <input type="date" name="date" id="daate" value="${.now?date}">
                                        <button onclick="sendDoctor()" type="submit" class="btn btn-primary" data-target="#myModal">
                                            Выбрать
                                        </button>
                                    </div>
                                </form>

                                aga:${.now?date}


                                <#list doctor.getDayScheduleList() as dsl>
<#--                                    ${dsl.getDate()?date("yyyy-mm-dd")}-->
<#--                                    ${dsl.getAppointments().size()}-->

<#--                                    <#if dsl.appointments.isEmpty()>-->
<#--                                        Талонов на это число нет-->
<#--                                    </#if>-->
                                    <#if date??>
                                        <#if dsl.getDate()?date("yyyy-mm-dd") == datee>
                                            <#list dsl.getAppointments() as aps>
                                                <#if aps.getState().toString() == "FREE">
                                                    <a href="login" class="btn btn-success mt-1"
                                                       role="button">${aps.getTime()}</a>
                                                <#else>
                                                    <a href="login" class="btn btn-danger mt-1"
                                                       role="button">${aps.getTime()}</a>
                                                </#if>

                                            </#list>
                                        </#if>
                                    </#if>

                                </#list>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                </div>
            </div>
        </div>
    </div>
</#macro>