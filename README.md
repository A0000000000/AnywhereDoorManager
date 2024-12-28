# AnywhereDoorManager

## AnywhereDoor后台管理
* 管理Plugin信息
* 管理Imsdk信息

## RESTful接口

### User相关
1. 注册
   * GET /user/register
   * params:
     * username: 用户名 QueryString
     * password: 密码 QueryString
   * result:
     * success: 返回token及flush_token
     * failed: 返回具体错误类型
2. 登录
   * GET /user/login
   * params:
     * username: 用户名 QueryString
     * password: 密码 QueryString
   * result:
     * success: 返回token及flush_token
     * failed: 返回具体错误类型
3. 刷新令牌
   * GET /user/flush_token
   * params:
     * token: 过期令牌
     * flush_token: 未过期的刷新令牌
   * result:
     * success: 返回token及flush_token
     * failed: 返回具体错误类型

### Imsdk相关
1. 查询一条记录
   * GET /imsdk/{id}
   * params:
     * id: imsdk id, 使用路径参数
   * result:
     * 成功: imsdk记录
     * 失败: 对应错误码
2. 查询所有记录
   * GET /imsdk
   * params: 无
   * result:
     * 成功: imsdk所有记录
     * 失败: 对应错误码
3. 创建一条记录
   * POST /imsdk/create
   * params: imsdk数据
   * result:
     * 成功: imsdk记录
     * 失败: 对应错误码
4. 更新一条记录
   * PUT /imsdk/update
   * params: imsdk新数据
   * result:
     * 成功: imsdk记录
     * 失败: 对应错误码
5. 删除一条记录
   * DELETE /imsdk/{id}
   * params: imsdk的id
   * result:
     * 成功: 被删除的数据记录
     * 失败: 对应错误码

### Plugin相关
1. 查询一条记录
    * GET /plugin/{id}
    * params:
        * id: plugin id, 使用路径参数
    * result:
        * 成功: plugin记录
        * 失败: 对应错误码
2. 查询所有记录
    * GET /plugin
    * params: 无
    * result:
        * 成功: plugin所有记录
        * 失败: 对应错误码
3. 创建一条记录
    * POST /plugin/create
    * params: plugin数据
    * result:
        * 成功: plugin记录
        * 失败: 对应错误码
4. 更新一条记录
    * PUT /plugin/update
    * params: plugin新数据
    * result:
        * 成功: plugin记录
        * 失败: 对应错误码
5. 删除一条记录
    * DELETE /plugin/{id}
    * params: plugin的id
    * result:
        * 成功: 被删除的数据记录
        * 失败: 对应错误码

### 配置中心
1. 根据id查询一条记录
   * GET /config/{id}
   * params:
     * id: 配置id，使用路径参数
   * result:
     * 成功: 返回config内容
     * 失败: 对应错误码
2. 根据config key查询一条记录
   * GET /config/{type}/{config key}
   * params:
     * type: 0: Plugin, 1: Imsdk
     * config key: 配置键
   * result:
     * 成功: 返回config内容
     * 失败: 对应错误码
3. 查询所有config
   * GET /config
   * result:
     * 成功: 返回config内容
     * 失败: 对应错误码
4. 新增一条config
    * POST /config/create
    * params: 使用json格式数据
      * type: 0: Plugin, 1: Imsdk
      * target_id: plugin/imsdk的Id
      * config_key: 配置键
      * config_value: 配置值
    * result:
      * 成功: 返回新建config内容
      * 失败: 对应错误码
5. 更新一条记录
   * PUT /config/update
   * params: 使用json格式数据
     * id: config id
     * config_key: 新的配置键
     * config_value: 新的配置值
   * result:
     * 成功: 返回更新后的config内容
     * 失败: 对应错误码
6. 删除一条记录
   * DELETE /config/{id}
   * params:
     * id: config id
   * result:
     * 成功: 返回被删除config内容
     * 失败: 对应错误码

## 环境变量
* DB_IP: 数据库IP地址
* DB_PORT: 数据库端口
* DB_NAME: 数据库名字, 即`anywhere_door`, 第一步中创建的数据库名字, 可以更换名字, 不建议
* DB_USER: 数据库用户
* DB_PASSWORD: 数据库密码

## 打包方式
1. 将代码仓库clone到本地
2. 安装docker及buildx
3. 打包镜像: 
    * `docker buildx build --platform linux/amd64 -t 192.168.25.5:31100/maoyanluo/anywhere-door-manager-test:1.0 . --load`

## 部署方式

### Docker Command Line
1. 创建容器:
    * `docker run --name anywhere-door-manager -itd -p 8080:80 -e DB_IP=ip -e DB_PORT=port -e DB_NAME=anywhere_door -e DB_USER=user -e DB_PASSWORD=pwd --restart=always 192.168.25.5:31100/maoyanluo/anywhere-door-manager:1.0`

### Kubernetes
```yaml
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: anywhere-door-manager-deployment
  namespace: anywhere-door
spec:
  replicas: 1
  selector:
    matchLabels:
      app: anywhere-door-manager
  template:
    metadata:
      labels:
        app: anywhere-door-manager
    spec:
      containers:
      - name: anywhere-door-manager
        image: 192.168.25.5:31100/maoyanluo/anywhere-door-manager:1.0
        imagePullPolicy: IfNotPresent
        env:
        - name: DB_IP
          value: "anywhere-door-mysql-service.anywhere-door"
        - name: DB_PORT
          value: "3306"
        - name: DB_NAME
          value: "anywhere_door"
        - name: DB_USER
          value: "user"
        - name: DB_PASSWORD
          value: "pwd"
        ports:
        - containerPort: 80
      restartPolicy: Always
---
apiVersion: v1
kind: Service
metadata:
  name: anywhere-door-manager-service
  namespace: anywhere-door
  labels:
    app: anywhere-door-manager
spec:
  type: NodePort
  ports:
  - port: 80
    targetPort: 80
    nodePort: 20080
  selector:
    app: anywhere-door-manager
```

## 使用
1. 创建一个用户: GET user/register?username=user&password=pwd
2. 登录: GET user/login?username=user&password=pwd
3. 以上两个接口都会返回token及flush_token, 通过携带token, 可以进一步创建imsdk或plugin