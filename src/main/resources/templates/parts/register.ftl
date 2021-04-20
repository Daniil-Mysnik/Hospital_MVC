<#macro page path>
    <div class="cotainer">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">Введите данные для регистрации</div>
                    <div class="card-body">
                        <form name="my-form" method="post" action="${requestcontext.contextPath}/${path}">
                            <div class="form-group row">
                                <label for="firstName" class="col-md-4 col-form-label text-md-right">Имя</label>
                                <div class="col-md-6">
                                    <input type="text" id="firstName" class="form-control" name="firstName">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="lastName" class="col-md-4 col-form-label text-md-right">Фамилия</label>
                                <div class="col-md-6">
                                    <input type="text" id="lastName" class="form-control" name="lastName">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="patronymic" class="col-md-4 col-form-label text-md-right">Отчество</label>
                                <div class="col-md-6">
                                    <input type="text" id="patronymic" class="form-control" name="patronymic">
                                </div>
                            </div>

                            <#nested >

                            <div class="form-group row">
                                <label for="login" class="col-md-4 col-form-label text-md-right">Логин</label>
                                <div class="col-md-6">
                                    <input type="text" id="login" class="form-control" name="login">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="password" class="col-md-4 col-form-label text-md-right">Пароль</label>
                                <div class="col-md-6">
                                    <input type="password" id="password" class="form-control" name="password">
                                </div>
                            </div>

                            <div class="col-md-6 offset-md-4">
                                <button type="submit" class="btn btn-primary">
                                    Зарегистрироваться
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>