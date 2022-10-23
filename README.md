<h2>Тестовое задание для Java разработчика</h2>

<h3>Преамбула</h3>
В игре есть такая механика как кланы, иначе говоря скопления игроков объединенные общей целью. У каждого клана есть своя казна с золотом. Есть различные способы пополнения казны клана. Можно выполнять задания, сражаться на арене, просто пополнить казну из своего кармана и т.д. И конечно же мы следим за всеми действиями, чтобы в случае чего служба поддержки могла как-то отвечать на вопросы пользователей в случае какого-то недопонимания.<br>

<h3>Задачи</h3>
1. Реализовать логику добавления/уменьшения золота в клан, при этом предусмотреть, что золото может зачислиться из сотни(100) разных потоков в один момент. Разными пользователями по разной причине.<br>
2. Реализовать отслеживание разных действий начисления золота, чтобы служба поддержки смогла идентифицировать когда и по какой причине в казне изменилось количества золота, сколько было, сколько стало и т.д.<br>

<h3>Ограничения</h3>
Задание нацелено на демонстрацию знаний java, в частности знание многопоточности.
Не стоит использовать высокоуровневые библиотеки такие как Spring, Hibernate. При необходимости лучше использовать что-то на уровне JDBC.

<h2>Реализация</h2>
В основу приложения положена следующая архитектура:

![app_architecture.jpg](/src/app_architecture.jpg)

Приложение содержит генератор тестовых данных для демонстрации своей работы.<br>
Тестовые данные обрабатываются приложением в многопоточном режиме и записываются в PostgreSQL.</br>
Метрики мониторинга действий по начислению золота сохраняются в Kafka. В случае недоступности Kafka, метрики сохраняются в резервной системе хранения. Для демонстрации идеи в качестве резервной системы хранения реализовано сохранение в ConcurrentHashMap.

<h3>Запуск приложения</h3>
Перед запуском приложения необходимо поднять docker-образы PostgteSQL и Kafka из файла docker-compose.yml в корне проекта.<br>

Работа с тестовыми данными занимает менее 2 минут.
Можно наблюдать сделующую картину<br>
в таблицах:
<br>
![img.png](/src/img.png)
<br>
![img_1.png](/src/img_1.png)
<br>
![img_2.png](/src/img_2.png)
<br>
в Kafka:
<br>
![img_3.png](/src/img_3.png)
<br>

<h3>Что не было сделано, но обязательно в работе</h3>
1. Прокрытие unit tests (до 95% кода)<br>
2. JavaDoc.<br>
