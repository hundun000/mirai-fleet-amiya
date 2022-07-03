### midi生成模块

本模块使用了[mider项目](https://github.com/whiterasbk/mider)提供的工具。

相关的`mider code语法`说明如下：

```shell
在 mider code 里, 称形如 >g>sequence 为一条轨道, 而形如 >!cmd> 为一条内建指令
# 轨道格式
>bpm[;mode][;pitch][;i=instrument][;timeSignature][;midi|img|pdf|mscz]>音名序列 | 唱名序列
bpm: 速度, 必选, 格式是: 数字 + b, 如 120b, 默认可以用 g(pitch=4&bpm=80) 或者 f(pitch=3&bpm=80) 代替
mode: 调式(若为小调则为同名小调), 可选, 格式是 b/#/-/+ 调式名, 如 Cminor, -Emaj, bC
pitch: 音域(音高), 可选, 默认为 4
i=instrument: 选择乐器, 可选
timeSignature: 拍号, 可选


## 关于音符序列

# 公用规则 (如无特殊说明均使用在唱名或音名后, 并可叠加使用)
 # : 升一个半音, 使用在音名或唱名前
 $ : 降一个半音, 使用在音名或唱名前
 + : 时值变为原来的两倍
 - : 时值变为原来的一半
 . : 时值变为原来的一点五倍
 : : 两个以上音符组成一个和弦
 ~ : 克隆上一个音符
 ^ : 克隆上一个音符, 并升高 1 度
 v : 克隆上一个音符, 并降低 1 度
 ↑ : 升高一个八度
 ↓ : 降低一个八度
 % : 调整力度, 后接最多三位数字
 & : 还原符号
类似的用法还有 m-w, n-u, q-p, i-!, s-z 升高或降低度数在 ^-v 的基础上逐步递增或递减

# 如果是音名序列则以下规则生效
a~g: A4~G4
A~G: A5~G5
 O : 二分休止符 
 o : 四分休止符 
0-9: 手动修改音域

# 如果是唱名序列则以下规则生效
1~7: C4~B4
 0 : 四分休止符
 i : 升高一个八度
 ! : 降低一个八度
 b : 降低一个半音, 使用在唱名前
 * : 后接一个一位数字表示重复次数
 
# 宏
目前可用的宏有
1. (def symbol=note sequence) 定义一个音符序列
2. (def symbol:note sequence) 定义一个音符序列, 并在此处展开
3. (=symbol) 展开 symbol 对应音符序列
4. (include path) 读取 path 代表的资源并展开, 如果是文件默认目录是插件的数据文件夹
5. (repeat time: note sequence) 将音符序列重复 times 次
6. (ifdef symbol: note sequence) 如果定义了 symbol 则展开
7. (if!def symbol: note sequence) 如果未定义 symbol 则展开
8. (macro name param1[,params]: note sequence @[param1]) 定义宏
9. (!name arg1[,arg2]) 展开宏
10. (velocity linear from~to: note sequence) 调整 note sequence 的力度, 仅适用于长音名序列
目前宏均不可嵌套使用
```

本模块并不支持某些`mider code`特性（包括不限于）:

```shell
midi: 是否仅上传 midi 文件, 可选  
img: 是否仅上传 png 格式的乐谱  
pdf: 是否仅上传 pdf 文件, 可选  
mscz: 是否仅上传 mscz 文件, 可选  
```

## 示例

```
1. 小星星
>g>1155665  4433221  5544332  5544332
等同于
>g>ccggaag+ffeeddc+ggffeed+ggffeed
等同于
>g>c~g~^~v+f~v~v~v+(repeat 2:g~v~v~v+) (酌情使用

2. KFC 可达鸭
>g;bE>g^m+C-wmD+D^m+G-wmE+D^w+C-wmD+DvagaC

3. 碎月 
>85b>F+^$BC6GFG C$E F D$ED$b C+ g$b C$E F$E F+ F$E F$B G++ G$B C6C6$B C6 G+ G$E FGF$E C+ C$b C+C$EF$EFG $E
等同于
>85b;Cmin>F+^BC6GFG CE F DEDb C+ gb CE FE F+ FE FB G++ GB C6C6B C6 G+ GE FGFE C+ Cb C+CEFEFG E

4. 生日快乐
>88b>d.d- e+v g+ #f++ d.d- e+v a+ v+ d.d- D+b+g+ #f+ e+ C.C- b+ g+^ v+

5. 茉莉花
>110b>e+em^m~wv+g^v++e+em^m~wv+g^v++g+~~em^+av~++e+d^m+evv+c^v++evvmv+.eg+amg++d+egd^cwv++ ^-c+d+.ec^vwv++

6. bad apple!
>100b>e#fgab+ ED b+ e+ b a-- B-- A- g#f e#fga b+ ag #fe#fg #f--G--#F-e #d#f e#fgab+ ED b+e+ ba--B--A- g#f e#fgab+ ag

7. Jingle Bells
>100b>E~~+E~~+EmC^^++F~~+Fv~+Ev~^ D+G+E~~+E~~+EmC^^++F~~+Fv~~m~vDv++

8. 两只老虎 卡农
>g;3>(def tiger:1231 1231 3450 3450 5-6-5-4-31 5-6-5-4-31 15!10 15!10)
>g;4>00(=tiger)
>g;5>0000(=tiger)
>g;6>000000(=tiger)
>g;7>00000000(=tiger)
```

本模块对于`mider项目`仅是简单使用，若想得到更复杂的midi功能， 可使用[MiraiMidiProduce插件](https://github.com/whiterasbk/MiraiMidiProduce)。更多示例见 [awesome-melody](https://github.com/whiterasbk/MiraiMidiProduce/tree/master/awesome-melody)

#### 【指令】midi生成

**<子指令>: midi**  
**<指令参数>: 一段mider code，可多行**

> -> /<角色名> midi >g>ccggaag+ffeeddc+ggffeed+ggffeed  
> <- [语音]

