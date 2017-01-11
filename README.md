# cruddao
Creates a dao object for java POJOs. provides following operations out of the box:

merge(T t)

insert (T t)

update (int id, T t)

get(int id)

List<T> getAll()

int delete(int id)

int deleteAll()

boolean isRecordExists(int id)

List<T> find(String[] fields, Object[] values)

Right now it works with h2 db (not tested with the others) hoping to create test cases to cover all major dbs.
