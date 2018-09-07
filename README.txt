Exhibition Calendar

ENG: System 'Exhibition calendar'. There is a list of exhibition halls in
each of which has a list of Expositions. The visitor buys
Tickets for payment and selecting the theme of the exhibition.
RU: Система Календарь выставок. Существует список Выставочных залов в
каждом из которых есть перечень Экспозиций. Посетитель покупает
Билеты оформив Платёж и выбрав Тему выставки.

Prerequisites
1. JDK: 1.8 or higher
2. MySQL: 8.*
3. Maven: 3.5.3 or higher
4. Recomended server: Apache Tomcat 9.0

INSTALING
1. Start MySQL server
2. Set to DB connection username: 'root' and password: '1123581321' or specify another values
   at */src/main/resourses/DBConnection.properties and */src/main/resourses/MySqlDB_TEST.properties 
3. Use the commands from the file */DB/ex.txt and create a main DB.
4. Use the commands from the file */DB/ex_test.txt and create a test DB.
5. Use the commands from the files */DB/dump_min.txt or */DB/dump.txt for insert data in DB.
6. Compile and package project.
7. Copy ec.war from */target to  */Tomcat/webapps or if you do not use the Tomcat, in the appropriate directory to your server

Running at lockal machine
1. Start MySQL server
2. Start server
3. Pproject available at your localhost/ec
4. Admin name: 'Admin' and password: 'Admin1'

Author
* Galomko Dmytro
