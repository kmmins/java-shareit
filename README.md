# java-shareit
Repository for study project:

    ┌───┬┐         ┌──┐┌┐ 
    │┌─┐││         └┤├┼┘└┐
    │└──┤└─┬──┬─┬──┐││└┐┌┘
    └──┐│┌┐│┌┐│┌┤│─┤││ ││ 
    │└─┘││││┌┐││││─┼┤├┐│└┐
    └───┴┘└┴┘└┴┘└──┴──┘└─┘⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀

These technologies were used for this project:
Spring Boot, Maven, Lombok, REST, CRUD, PostgreSQL, Jakarta Persistence API, Query Methods, JPQL Query.

[ER-diagram](https://github.com/kmmins/java-shareit/blob/add-bookings/assets/shareit.md):
```mermaid
classDiagram
direction BT
class bookings {
   bigint item_id
   timestamp start_time
   timestamp end_time
   bigint booker_id
   booking_status status
   bigint id
}
class comments {
   varchar(512) comment_text
   bigint item_id
   bigint author_id
   timestamp created
   bigint id
}
class items {
   varchar(255) item_name
   varchar(512) description
   boolean available
   bigint owner_id
   bigint request_id
   bigint id
}
class requests {
   varchar(512) description
   bigint request_user_id
   bigint id
}
class users {
   varchar(255) user_name
   varchar(512) email
   bigint id
}

bookings  -->  items : item_id
bookings  -->  users : booker_id
comments  -->  items : item_id
comments  -->  users : author_id
items  -->  requests : request_id
items  -->  users : owner_id
requests  -->  users : request_user_id
```