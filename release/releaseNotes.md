## v0.8.1

开发者测试时使用的mirai-console版本：2.11.0（建议用户不要落后太多版本）

### 修复

- 修复0.8.0：提醒模块配置随机内容未正确执行

### ConfigAndData变化：  

- 无

## v0.8.0

开发者测试时使用的mirai-console版本：2.11.0（建议用户不要落后太多版本）

### 修复

- 修复微博图片下载失败问题。

### 新特性（对比0.7.0）

- 支持配置微博文案模板。配置文件格式变动，可兼容上一版本自动补充配置文件。
- midi功能引入文本参数转义。
- 《功能基础说明》等文档描述优化，新增`Debug级指令`概念。本插件的某些模块也提供`Debug级指令`，详见各模块文档。

### ConfigAndData变化：  

- 无

## v0.7.1

开发者测试时使用的mirai-console版本：2.11.0（建议用户不要落后太多版本）

### 修复

- 修复`v0.7.0`起`阿米娅闲聊`的戳一戳回复图片未正常工作。
- 修复`v0.6.0`移除`搜音乐`功能但未成功移除。

### ConfigAndData变化：  

- 将`data\hundun.fleet.amiya\ReminderFunction\audios`中样例文件改为PC端可听的`amr`格式

## v0.7.0

开发者测试时使用的mirai-console版本：2.11.0（建议用户不要落后太多版本）

### 新特性（对比0.6.0）

- 升级`报时与提醒`功能，支持发送音频和图片。配置文件格式变动，详见具体模块文档。

### 不兼容注意事项

- `报时与提醒`功能配置文件格式变动，需按新格式修改相关配置文件，或者删除等待自动创建默认值。

### ConfigAndData变化：  

- 新增`报时与提醒`默认配置所需要的音频和图片

## v0.6.1

开发者测试时使用的mirai-console版本：2.11.0（建议用户不要落后太多版本）

### 修复

- 修复`v0.6.0`的`权限管理功能`中的`群开关指令`未正常工作。（`群开关Console指令`可正常工作）

### ConfigAndData变化：  

- 无

## v0.6.0

开发者测试时使用的mirai-console版本：2.11.0（建议用户不要落后太多版本）

### 新特性

- 新增`权限管理`功能，配置权限更方便
- 移除`搜音乐`功能，因为并不能单独控制单个模块的权限，而发送卡片存在风险
- 新增`midi制作`功能

### 不兼容注意事项

- 权限节点变动以便更好地管理。旧版用户应阅读新版权限配置有关说明，并作出相应改动。否则对于新版插件权限判断时，属于未启用状态。
- `阿米娅画图`所需要的data内容变动不兼容。需要手动覆盖`data\hundun.fleet.amiya\SharedPetFunction\petService\templates`

### ConfigAndData变化：  

- `阿米娅画图`所需要的data内容变动；

## v0.5.2

开发者测试时使用的mirai-console版本：2.11.0（建议用户不要落后太多版本）

### 新特性：

- 不再依赖前置插件`SkikoMirai`
- 新功能：`阿米娅画图`

### ConfigAndData变化：  

- 新增`阿米娅画图`所需要的data；
- `阿米娅闲聊子功能：摸头`所需要的data位置和形式变动；

## v0.5.1

开发者测试时使用的mirai-console版本：2.11-RC（建议用户不要落后太多版本）

### 修复

- 修复音乐指令不生效
- 修复阿米娅闲聊子功能：海猫all不生效

### ConfigAndData变化：  

无

## v0.5.0

开发者测试时使用的mirai-console版本：2.11-RC（建议用户不要落后太多版本）

### 注意事项

新增依赖前置插件：SkikoMirai，用户需要做的工作详见首页README说明。

### 新特性：
- 新功能：阿米娅闲聊子功能：海猫all，以摸头图片回应戳一戳

### 优化：
- 用户文档调整
- framework内部实现优化

### ConfigAndData变化：  

新增patpat所需要的data

## v0.2.3

新特性：
- 新功能：搜音乐、立刻聊天、帮助

优化：
- 用户文档改变。变为引用框架的文档；不再“提供两套等效的`<指令名>`”，变为“所有指令都统一使用`<角色名>`作为`<指令名>`”
- 使用mirai提供的线程调度器

ConfigAndData变化：  
无


## v0.2.2

修复：
- 修复自定义回复 #1 

ConfigAndData变化：  
无

## v0.2.1

修复：
- 修复会尝试把`data\hundun.fleet.amiya\AmiyaChatFunction\selfNudgeFaces`中的`.gitkeeper`文件作为表情图片发送然后报错的问题。
- 修复样例中的自定义事项提醒关于星期数的配置错误及对应文档错误
 
新特性&ConfigAndData变化：
- 不再随release提供大部分config和data文件夹压缩包。因为插件在启动后，若发现没有对应配置文件，会使用预设的样例值新建文件。故现在仅需要提供图片和音频等data文件。
   <br>对于老用户：各配置文件已存在，无事发生。也可以手动删除某个配置文件，待插件自动新建，得到样例值。
   <br>对于新用户：按说明文档操作即可。