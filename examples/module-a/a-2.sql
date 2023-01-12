-- MyBatis Thymeleaf 壊れた 2-way SQL
UPDATE
    /*[# th:if="${firstName} != null"]*/
    shared_table4
    /*[/]*/
    /*[# th:unless="${firstName} != null"]*/
    shared_table5
    /*[/]*/

SET id = id,
    /*[# th:if="${firstName} != null"]*/
    firstName = /*[# mb:p="firstName"]*/ 'Taro' /*[/]*/
    /*[/]*/
    /*[# th:unless="${firstName} != null"]*/
    firstName = /*[# mb:p=null]*/ 'Taro' /*[/]*/
    /*[/]*/
WHERE id = /*[# mb:p="id"]*/ 1 /*[/]*/
