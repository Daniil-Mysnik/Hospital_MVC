<#import "parts/common.ftl" as c>

<@c.page>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-lg-10 col-xl-8 mx-auto">
                <div class="my-4">
                    <ul class="nav nav-tabs mb-4" id="myTab" role="tablist">
                        <li class="nav-item">
                            <a class="nav-link active" id="home-tab" data-toggle="tab" role="tab"
                               aria-controls="home" aria-selected="false">Профиль</a>
                        </li>
                    </ul>
                    <#if account.userType.toString() == "PATIENT">
                        <#assign path = "patientUpdate">
                    <#elseif account.userType.toString() == "ADMIN">
                        <#assign path = "adminUpdate">
                    <#else>
                        <#assign path = "#">
                    </#if>
                    <form method="post" action="${requestcontext.contextPath}/${path}">
                        <div class="row mt-5 align-items-center">
                            <div class="col">
                                <div class="row align-items-center">
                                    <div class="col-md-7">
                                        <h4 class="mb-1">${account.lastName}, ${account.firstName}</h4>
                                        <p class="small mb-1"><span class="badge badge-dark">${account.userType}</span>
                                        </p>
                                        <#if account.userType.toString() == "ADMIN">
                                            <p class="small mb-1"><span
                                                        class="badge badge-dark">${account.position}</span></p>
                                        </#if>
                                        <#if account.userType.toString() == "DOCTOR">
                                            <p class="small mb-1"><span
                                                        class="badge badge-dark">${account.getSpeciality()}</span></p>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <hr class="my-4"/>
                        <div class="form-row">
                            <div class="form-group col-md-4">
                                <label for="firstName">Имя</label>
                                <input name="firstName" type="text" class="form-control"
                                       placeholder="${account.getFirstName()}"/>
                            </div>
                            <div class="form-group col-md-4">
                                <label for="lastName">Фамилия</label>
                                <input name="lastName" type="text" class="form-control"
                                       placeholder="${account.getLastName()}"/>
                            </div>
                            <#if account.getPatronymic()??>
                                <div class="form-group col-md-4">
                                <label for="patronymic">Отчество</label>
                                <input name="patronymic" type="text" class="form-control"
                                       placeholder="${account.getPatronymic()}"/>
                            </div>
                            </#if>
                        </div>
                        <#if account.userType.toString() == "PATIENT">
                            <div class="form-group">
                                <label for="email">Электронный адрес</label>
                                <input type="email" class="form-control" id="email"
                                       placeholder="${account.getEmail()}"/>
                            </div>
                            <div class="form-group">
                                <label for="address">Адрес</label>
                                <input type="text" class="form-control" id="address"
                                       placeholder="${account.getAddress()}"/>
                            </div>
                            <div class="form-group">
                                <label for="phone">Телефон</label>
                                <input type="phone" class="form-control" id="phone"
                                       placeholder="${account.getPhone()}"/>
                            </div>
                        </#if>
                        <#if account.userType.toString() == "ADMIN">
                            <div class="form-group">
                                <label for="position">Новая должность</label>
                                <input type="text" class="form-control" id="position" name="position"
                                       placeholder="${account.getPosition()}"/>
                            </div>
                        </#if>

                        <hr class="my-4"/>
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="oldPassword">Старый пароль</label>
                                    <input type="password" class="form-control" id="oldPassword" name="oldPassword"/>
                                </div>
                                <div class="form-group">
                                    <label for="newPassword">Новый пароль</label>
                                    <input type="password" class="form-control" id="newPassword" name="newPassword"/>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <p class="mb-2">Требования к паролю</p>
                                <p class="small text-muted mb-2">Для создания нового пароля, он должен соответствовать
                                    требованиям:</p>
                                <ul class="small text-muted pl-4 mb-0">
                                    <li>Минимум 10 символов</li>
                                    <li>Максимум 50 символов</li>
                                </ul>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">Сохранить изменения</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@c.page>