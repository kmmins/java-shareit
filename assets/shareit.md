classDiagram
direction BT
class bookings {
   timestamp start_time
   timestamp end_time
   booking_status status
   bigint booker_id
   bigint item_id
   bigint id
}
class comments {
   varchar(512) comment_text
   timestamp created
   bigint author_id
   bigint item_id
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
   timestamp with time zone created
   bigint id
}
class users {
   varchar(255) user_name
   varchar(512) email
   bigint id
}

bookings  -->  items : item_id:id
bookings  -->  users : booker_id:id
comments  -->  items : item_id:id
comments  -->  users : author_id:id
items  -->  requests : request_id:id
items  -->  users : owner_id:id
requests  -->  users : request_user_id:id
