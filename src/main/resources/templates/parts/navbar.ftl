<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="${requestcontext.contextPath}/main">Hospital</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="${requestcontext.contextPath}/byDoctor">Расписание</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Список</a>
                </li>
                <#if login??>
                    <#if login.getUserResponse().userType.toString() == "ADMIN">
                        <div class="dropdown">
                            <a class="nav-link" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Зарегистрировать
                            </a>

                            <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                                <a class="dropdown-item" href="${requestcontext.contextPath}/registerDoctor">Доктора</a>
                                <a class="dropdown-item" href="${requestcontext.contextPath}/patientRegister">Пациента</a>
                                <a class="dropdown-item" href="${requestcontext.contextPath}/adminRegister">Администратора</a>
                            </div>
                        </div>
                    </#if>
                </#if>
            </ul>
        </div>
        <div class="w-50">
            <x-clock></x-clock>
        </div>
        <#if login??>
            <div class="navbar-text mr-3">
                <a href="${requestcontext.contextPath}/account">${login.getUserResponse().getFirstName()}
                    , ${login.getUserResponse().getLastName()}</a>
            </div>
            <div>
                <form method="post" action="${requestcontext.contextPath}/logout">
                    <input type="hidden" name="sessionId" value="${login.getSessionId()}">
                    <button class="btn btn-outline-primary" role="button" type="submit">Выход</button>
                </form>
            </div>
        <#else>
            <div class="text-right">
                <a href="login" class="btn btn-outline-primary" role="button">Вход</a>
                <a href="patientRegister" class="btn btn-primary" role="button">Регистрация</a>
            </div>
        </#if>
    </div>
</nav>

<script type="text/javascript" src="https://bootstraptema.ru/plugins/2015/x-tag/x-tag-core.min.js"></script>

<style>
    x-clock {
        color: rgb(148, 148, 165);
        font-size:20px;
    }
</style>

<script>
    xtag.register('x-clock', {
        lifecycle: {
            created: function(){
                this.start();
            }
        },
        methods: {
            start: function(){
                this.update();
                this.xtag.interval = setInterval(this.update.bind(this), 1000);
            },
            stop: function(){
                this.xtag.interval = clearInterval(this.xtag.interval);
            },
            update: function(){
                this.textContent = new Date().toLocaleTimeString();
            }
        },
        events: {
            tap: function(){
                if (this.xtag.interval) this.stop();
                else this.start();
            }
        }
    });
</script>