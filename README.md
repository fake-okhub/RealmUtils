# RealmUtils
it about simple for realm use about CRUD.
For example:
this is object 

```
public class User extends RealmObject {
    @PrimaryKey
    private int id;
    private int age;
    @Required
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```
add:
```
final User user=new User();
user.setId(4);
user.setAge(19);
user.setName("jack");
RealmHelper.getInstance().add(user);
```
delete:
```
RealmHelper.getInstance().deleteAll(User.class);
```
query:
```
List<User> users = (List<User>) RealmHelper.getInstance().queryAll(User.class);
```
update:  
just modify one parameter:
```
RealmHelper.getInstance().updateParamByPrimaryKey(User.class,"id",4,"setAge",88);
```
modify some parameter:
``` 
Map<String ,Object> params=new HashMap<>();
params.put("setAge",99);
params.put("setName","2b");
RealmHelper.getInstance().updateParamByPrimaryKey(User.class,"id",4,params);
```


        

