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


## 部署方式
1. 将代码仓库clone到本地
2. 安装docker及buildx
3. 打包镜像: 
    * `docker buildx build --platform linux/amd64 -t 192.168.25.5:31100/maoyanluo/anywhere-door-manager-test:1.0 . --load`
4. 创建容器:
    * `docker run --name anywhere-door-manager -itd -p 8080:80 -e DB_IP=192.168.25.7 -e DB_PORT=3306 -e DB_NAME=anywhere_door -e DB_USER=root -e DB_PASSWORD=09251205 --restart=always 192.168.25.5:31100/maoyanluo/anywhere-door-manager:1.0`