# JdbcUtils使用指南
`JdbcUtils`是JDBC的工具类，封装了JDBC连接获取、语句构造、资源释放等繁琐步骤，对外提供简洁的数据库查询和更新操作接口。
## 使用前准备
1. 添加maven仓库地址
```xml
<repositories>
    <repository>
        <id>byx-maven-repo</id>
        <name>byx-maven-repo</name>
        <url>https://gitee.com/byx2000/maven-repo/raw/master/</url>
    </repository>
</repositories>
```
2. 添加maven依赖
```xml
<dependencies>
    <dependency>
        <groupId>byx.util</groupId>
        <artifactId>JdbcUtils</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```
3. 在`resources`目录下新建一个`db.properties`文件，写入如下配置：

|配置|说明|
|-|-|
|`jdbc.driver`|数据库驱动类名|
|`jdbc.url`|连接字符串|
|`jdbc.username`|用户名|
|`jdbc.password`|密码|
## 查询操作
`JdbcUtils`中包含了若干形如`queryXXX`的查询方法，具体说明如下：
|方法|说明|
|-|-|
|`T query(String sql, RecordMapper<T> recordMapper, Object... params)`|查询数据库并转换结果集|
|`List<T> queryList(String sql, RowMapper<T> rowMapper, Object... params)`|查询数据库，对结果集的每一行进行转换，然后将所有行封装成列表|
|`List<T> queryList(String sql, Class<T> type, Object... params)`|查询数据库，将结果集的每一行转换成JavaBean，然后将所有行封装成列表|
|`T querySingleValue(String sql, Class<T> type, Object... params)`|查询数据库，返回结果集中的单个值|
|`T querySingleRow(String sql, RowMapper<T> rowMapper, Object... params)`|查询数据库，转换结果集中的单行数据|
|`T querySingleRow(String sql, Class<T> type, Object... params)`|查询数据库，将结果集中的单行数据转换成JavaBean|
## 结果集转换器
结果集转换器用于对查询结果进行转换，用户可实现自己的结果集转换器，也可使用预定义的结果集转换器，下面是所有预定义的结果集转换器：
|结果集转换器|说明|
|-|-|
|`ListRecordMapper<T>`|将整个结果集转换成列表|
|`SingleRowRecordMapper<T>`|从结果集中提取单行数据|

如果要自定义结果集转换器，则需要实现`RecordMapper<T>`接口：
```java
public interface RecordMapper<T>
{
    T map(Record record);
}
```
`map`方法的`Record`参数封装了JDBC中的`ResultSet`，其方法说明如下：
|方法|说明|
|-|-|
|`Row getCurrentRow()`|获取当前行|
|`boolean next()`|移动到下一行|

更多细节见下面的使用示例。
## 行转换器
行转换器用于对结果集中的一行进行转换，用户可实现自己的行转换器，也可使用预定义的行转换器，下面是所有预定义的行转换器：
|行转换器|说明|
|-|-|
|`BeanRowMapper<T>`|将一行数据转换成JavaBean|
|`MapRowMapper`|将一行数据转换成`Map`|
|`SingleColumnRowMapper<T>`|提取行中的单独一列|

如果要自定义行转换器，则需要实现`RowMapper<T>`接口：
```java
public interface RowMapper<T>
{
    T map(Row row);
}
```
`map`方法的`Row`参数封装了结果集中的一行数据，其部分方法说明如下：
|方法|说明|
|-|-|
|`Object getObject(String columnLabel)`|根据列名获取列值|
|`Object getObject(int columnIndex)`|根据列索引号获取列值（从1开始）|
|`int getColumnCount()`|获取列数|
|`String getColumnLabel(int index)`|根据列索引获取列标签（列名）|

更多细节见下面的使用示例。
## 更新操作
下面是`JdbcUtils`中用于更新操作的方法：
|方法|说明|
|-|-|
|`int update(String sql, Object... params)`|更新数据库，返回影响行数|
## 使用示例
### 查询
#### 1. 查询所有用户
```java
// 方法1：使用预定义的RecordMapper和RowMapper
List<User> users = JdbcTemplate.query("SELECT * FROM users",
                new ListResultSetMapper<>(new BeanRowMapper<>(User.class)));

// 方法2：使用自定义RecordMapper
List<User> users = JdbcUtils.query("SELECT * FROM users", record ->
{
    List<User> us = new ArrayList<>();
    while (record.next())
    {
        Row row = record.getCurrentRow();
        User u = new User();
        u.setId(row.getInt("id"));
        u.setUsername(row.getString("username"));
        u.setPassword(row.getString("password"));
        us.add(u);
    }
    return us;
});

// 方法3：使用自定义RowMapper
List<User> users = JdbcUtils.queryList("SELECT * FROM users", row ->
{
    User u = new User();
    u.setId(row.getInt("id"));
    u.setUsername(row.getString("username"));
    u.setPassword(row.getString("password"));
    return u;
});
```
#### 2. 查询id大于5的所有用户
```java
List<User> users = JdbcTemplate.query("SELECT * FROM users WHERE id > ?",
                new ListResultSetMapper<>(new BeanRowMapper<>(User.class))，
                5);
```
#### 3. 查询id等于1001的用户
```java
// 方法1
User user = JdbcUtils.querySingleRow("SELECT * FROM users WHERE id = 1001",
                new BeanRowMapper<>(User.class));

// 方法2
User user = JdbcUtils.querySingleRow("SELECT * FROM users WHERE id = 1001",
                User.class);
```
#### 4. 查询用户总数
```java
Integer count = JdbcUtils.querySingleValue("SELECT COUNT(*) FROM users", 
                Integer.class);
```
### 更新
#### 1. 插入用户
```java
int count = JdbcUtils.update("INSERT INTO users(username, password) VALUES(?, ?)",
                "byx", "123456");
```
#### 2. 删除用户
```java
int count = JdbcUtils.update("DELETE FROM users WHERE username = ? AND password = ?",
                "byx", "123456");
```