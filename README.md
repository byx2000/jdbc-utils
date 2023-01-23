# JdbcUtils使用指南

`JdbcUtils`是JDBC的工具类，封装了JDBC连接获取、语句构造、资源释放、事务控制等操作，对外提供简洁的数据库操作接口。

## 创建JdbcUtils

通过将一个`DataSource`实例传入`JdbcUtils`的构造函数来创建一个`JdbcUtils`实例：

```java
public DataSource dataSource() {
    // 返回一个DataSource
    // ...
}

JdbcUtils jdbcUtils = new JdbcUtils(dataSource());
```

## 查询操作

`JdbcUtils`中包含了若干形如`queryXXX`的查询方法，具体说明如下：

|方法|说明|
|---|---|
|`T query(String sql, ResultMapper<T> resultMapper, Object... params)`|查询数据库并转换结果集|
|`List<T> queryList(String sql, RowMapper<T> rowMapper, Object... params)`|查询数据库，对结果集的每一行进行转换，然后将所有行封装成列表|
|`List<T> queryList(String sql, Class<T> type, Object... params)`|查询数据库，将结果集的每一行转换成JavaBean，然后将所有行封装成列表|
|`T querySingleValue(String sql, Class<T> type, Object... params)`|查询数据库，返回结果集中的单个值|
|`T querySingleRow(String sql, RowMapper<T> rowMapper, Object... params)`|查询数据库，转换结果集中的单行数据|
|`T querySingleRow(String sql, Class<T> type, Object... params)`|查询数据库，将结果集中的单行数据转换成JavaBean|

## 结果集转换器

结果集转换器用于对查询结果进行转换，用户可实现自己的结果集转换器，也可使用预定义的结果集转换器，下面是所有预定义的结果集转换器：

|结果集转换器|说明|
|---|---|
|`ListResultMapper<T>`|将整个结果集转换成列表|
|`SingleRowResultMapper<T>`|从结果集中提取单行数据|

如果要自定义结果集转换器，则需要实现`ResultMapper<T>`接口：

```java
public interface ResultMapper<T> {
    T map(ResultSet rs);
}
```

更多细节见下面的使用示例。

## 行转换器

行转换器用于对结果集中的一行进行转换，用户可实现自己的行转换器，也可使用预定义的行转换器，下面是所有预定义的行转换器：

|行转换器|说明|
|---|---|
|`BeanRowMapper<T>`|将一行数据转换成JavaBean|
|`MapRowMapper`|将一行数据转换成`Map`|
|`SingleColumnRowMapper<T>`|提取行中的单独一列|

如果要自定义行转换器，则需要实现`RowMapper<T>`接口：

```java
public interface RowMapper<T> {
    T map(ResultSet rs);
}
```

更多细节见下面的使用示例。

## 更新操作

下面是`JdbcUtils`中用于更新操作的方法：

|方法|说明|
|---|---|
|`int update(String sql, Object... params)`|更新数据库，返回影响行数|

## 事务操作

`JdbcUtils`提供以下方法来实现事务的开启、提交和回滚：

|方法|说明|
|---|---|
|`public void startTransaction()`|开启事务|
|`public void commit()`|提交事务|
|`public void rollback()`|回滚事务|

## 使用示例

### 查询

#### 1. 查询所有用户

```java
// 方法1：使用预定义的ResultMapper和RowMapper
List<User> users = jdbcUtils.query("SELECT * FROM users",
                new ListResultMapper<>(new BeanRowMapper<>(User.class)));

// 方法2：使用自定义ResultMapper
List<User> users = jdbcUtils.query("SELECT * FROM users", record ->
{
    List<User> us = new ArrayList<>();
    while (rs.next()) {
    User u = new User();
    u.setId(rs.getInt("id"));
    u.setUsername(rs.getString("username"));
    u.setPassword(rs.getString("password"));
    us.add(u);
    }
    return us;
});

// 方法3：使用自定义RowMapper
List<User> users = jdbcUtils.queryList("SELECT * FROM users", row ->
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
List<User> users = jdbcUtils.query("SELECT * FROM users WHERE id > ?",
                new ListResultSetMapper<>(new BeanRowMapper<>(User.class))，
                5);
```

#### 3. 查询id等于1001的用户

```java
// 方法1
User user = jdbcUtils.querySingleRow("SELECT * FROM users WHERE id = 1001",
                new BeanRowMapper<>(User.class));

// 方法2
User user = jdbcUtils.querySingleRow("SELECT * FROM users WHERE id = 1001",
                User.class);
```

#### 4. 查询用户总数

```java
Integer count = jdbcUtils.querySingleValue("SELECT COUNT(*) FROM users", 
                Integer.class);
```

### 更新

#### 1. 插入用户

```java
int count = jdbcUtils.update("INSERT INTO users(username, password) VALUES(?, ?)",
                "byx", "123456");
```

#### 2. 删除用户

```java
int count = jdbcUtils.update("DELETE FROM users WHERE username = ? AND password = ?",
                "byx", "123456");
```

### 事务操作

```java
jdbcUtils.startTransaction(); // 开启事务
// ...
jdbcUtils.commit(); // 提交事务
// ...
jdbcUtils.rollback(); // 回滚事务
```