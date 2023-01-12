-- MyBatis Thymeleaf 壊れた 2-way SQL
UPDATE shared_table4
SET id = id
    /*[# th:if="${firstName} != null"]*/
  , firstName = "nonnull"
    /*[/]*/
    /*[# th:unless="${firstName} != null"]*/
  , firstName = "null"
WHERE id = /*[# mb:p="id"]*/ 1 /*[/]*/;

UPDATE shared_table5
SET id = id
    /*[# th:if="${firstName} != null"]*/
  , firstName = "nonnull"
    /*[/]*/
    /*[# th:unless="${firstName} != null"]*/
  , firstName = "null"
WHERE id = /*[# mb:p="id"]*/ 1 /*[/]*/;
