> 虚拟机参数:   禁止屏幕缩放，加 -Dprism.allowhidpi = false
>
> 起始缩放比例：-Dglass.win.miniHiDPI = 1
>
> 多倍图 ： 图片命名方式aaa@2x.png 会自动切换

## 一 窗口Stage

start方法默认会传入一个Stage，这个就是窗口对象

```java
public void start(Stage primaryStage) throws Exception{
```

### 1 Application生命周期

新建项目会有三个部分，继承了`Application`的main，controller，还有fxml，其中Application的生命周期为：

* 四个方法执行的顺序为：`main--->init-->start-->stop`

* 可以看到`start`，和`stop`是另起的线程，`init`也是独立的一个线程

```java
public class Main extends Application {
    public void init() throws Exception 

    public void start(Stage primaryStage)

    public void stop()
   
    public static void main(String[] args)
}
```

<img src="http://md.yangcc.top/%E6%88%AA%E5%9B%BE%E5%BD%95%E5%B1%8F_%E9%80%89%E6%8B%A9%E5%8C%BA%E5%9F%9F_20210125173242.png" alt="截图录屏_选择区域_20210125173242" style="zoom:50%;" />

### 2 窗口操作

涉及到一堆的set方法，还是没啥难度的，这里的监听不只能检测宽高，还有很多，只要找property的就可以添加

```java
    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Hello World");        // 设置窗体标题

        primaryStage.setIconified(true);        // 设置最小化,就是打开的时候就是最小化状态

        primaryStage.setMaximized(true);        // 设置最大化，就是打开的时候就是最大化

        primaryStage.setFullScreen(true);        // 设置全屏
        primaryStage.setScene(new Scene(new Group()));

        primaryStage.close();        // 关闭
        
                
        primaryStage.setX(300);  // 设置窗口出现的位置
        primaryStage.setY(300);

        primaryStage.setResizable(false);        // 窗口是否可调整大小
        // 设置窗口不透明度,1就是完全不透明，0是直接透明看不到了
        primaryStage.setOpacity(0.8);
        
        // 设置总是在其他窗口之上，点击别的软件，此窗口也不会居于其他窗口下面
        primaryStage.setAlwaysOnTop(true);
        
        // 设置宽高,这个是最高级别的设置，会覆盖掉上面展示fxml的那个设置的大小
        primaryStage.setHeight(2000);
        primaryStage.setWidth(2000);

        // 监听高的变化,宽类似
        primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // 获取最新的高度，然后以双精度的浮点接收
                System.out.println(newValue.doubleValue());
            }
        });
        // 显示窗口
        primaryStage.show();
    }
```

###　3 窗口样式

一般不会用，就用默认的不好吗

```java
primaryStage.initStyle(StageStyle.DECORATED); // 纯白装饰的窗口，默认的
primaryStage.initStyle(StageStyle.UNDECORATED);// 纯白的，并且去掉缩小，放大,关闭的窗口
primaryStage.initStyle(StageStyle.TRANSPARENT); // 透明背景的窗口
primaryStage.initStyle(StageStyle.UTILITY); // 纯白背景的，只带关闭按钮的窗口
primaryStage.initStyle(StageStyle.UNIFIED);// 使用当前操作系统的窗口样式，不支持的话会降级为默认的
```

### 4 窗口模态

很常见的功能，软件点设置出现一个窗口，然后不关这个窗口，原来的窗口无法操作，这个`Modality`就可以实现这个功能。

```java
// 比如有两个窗口。s1,s2不关闭窗口s2就无法操作s1,有两种方式

// 方式1
s2.initModality(Modality.APPLICATION_MODAL);

// 方式2
s2.initOwner(s1);
s2.initModality(Modality.WINDOW_MODAL);
```

### 5 应用程序平台支持类

`Platform`：这个类是，应用程序平台支持类。

`runter`：是一个队列，与外部同属于一个线程，但是会于最后执行，可以用于刷新页面

```java
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // runlater
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                System.out.println("runLater里面的,线程名为："+name);
            }
        });
        String name = Thread.currentThread().getName();
        System.out.println("外面的,线程名为："+name);

    }
```

`isSupported()`：此方法判断电脑是否支持javafx的些东西：返回一个布尔值

比如，是否支持javafx的3d：

```java
boolean supported = Platform.isSupported(ConditionalFeature.SCENE3D);
```

### 6 查看屏幕信息类Screen

获取系统，屏幕列表，dpi等等

```java
// 获取主屏幕对象
Screen primary = Screen.getPrimary();
// 获取屏幕全部大小
Rectangle2D bounds = primary.getBounds();
// 获取用户可看到的屏幕大小
Rectangle2D visualBounds = primary.getVisualBounds();
// 获取屏幕dpi
double dpi = primary.getDpi();
```

可以看到屏幕的信息，可以根据这个调整窗口的位置了，因为dork栏的原因，这个可用高度少了点

## 二 杂七杂八的东西

### 1 界面整体

界面是一层一层的，窗口里面要有一个幕布，然后是控制控件布局的的一层，最后才是按钮

> stage-->scene-->group-->button

```java
  public void start(Stage primaryStage) throws Exception{
        // 按钮
        Button button = new Button("按钮");
        button.setPrefWidth(400);
        button.setPrefHeight(100);
        //group
        Group group = new Group();
        group.getChildren().add(button);
        // scene
        Scene scene = new Scene(group,1000,1000);
        primaryStage.setScene(scene);

        primaryStage.setTitle("窗口");
        primaryStage.show();

    }
```

`鼠标样式`，默认很多的样式，小手，拖动的箭头等等，也可以加载图片：注意java这个`toExternalForm()`此方法跟`getPath()`不一样，`getPath`只是路径，而`toExternalForm`在路径前还有个`file:`这个东西

```java
button.setCursor(Cursor.CLOSED_HAND); // 鼠标变小手
// 加载图片的url
URL url = this.getClass().getResource("/img/img.png");
String s = url.toExternalForm(); //构造此URL的字符串表示形式
button.setCursor(Cursor.cursor(s)); // 鼠标移到上面会变样式
```

`使用默认浏览器打开网址`：deepin下没成功，找不到浏览器就很离谱

```java
// 打开百度
HostServices hostServices =getHostServices();
hostServices.showDocument("www.baidu.com");
```

### 2 容器group

group相当于一个容器，可以讲组件放进去，然后在group中对子组件进行管理，添加，删除，设置样式，等等

* ` public ObservableList<Node> getChildren()`此方法获取到一个子组件列表，可以进行管理

```java
// 检测子组件的边界位置，返回布尔值
boolean contains = group.contains(0, 20);
// 给父组件设置属性，子组件自动会设置
group.setOpacity(0.5);
// 也可以获取到子组件的数组，然后获取属性，以及统一设置
Object[] obj = group.getChildren().toArray();
int length = obj.length; // 有几个子组件
for (Object o : obj) {
    Button button = (Button) o;
    button.setPrefWidth(100);// 比如设置首选宽度，等等
}

// 自动管理子组件大小,默认就是true
group.setAutoSizeChildren(true);

// 移除按钮
group.getChildren().remove(0);
```

### 

## 三 控件

### 1 按钮

####　1.1 点击按钮

`创建按钮`设置位置：

```java
Button button = new Button("按钮");
// 位置
button.setLayoutX(300);
button.setLayoutY(10);
// 长宽
button.setPrefWidth(200);
button.setPrefHeight(50);
```

`设置字体，以及字的大小,颜色`

```java
button.setFont(Font.font("PingFang SC Regular",30));
button.setTextFill(Paint.valueOf("#FF190F"));
```

`设置背景色，圆角，内边距`

* 这里还可以设置透明度`Paint.valueOf("#BFA66A")`，前六位是颜色，后两位可以设置透明度，比如：00全透明，55半透明

```java
// 设置圆角,背景
BackgroundFill backgroundFill =
    new BackgroundFill(Paint.valueOf("#BFA66A"), // 颜色
                       new CornerRadii(15),   // 圆角
                       new Insets(0)); // 看着像是内边距
Background background = new Background(backgroundFill);
button.setBackground(background);
//button.setBackground(Background.EMPTY);// 背景为空
```

`设置按钮边框`

```java
// 设置边框
button.setBorder(new Border(new BorderStroke(
    Paint.valueOf("#000000"), // 颜色
    BorderStrokeStyle.SOLID, // 实线，虚线，之类的
    new CornerRadii(15), // 圆角
    new BorderWidths(10)  // 边框的宽度
)));
```

`使用css设置样式`

作为web非常简单好用的css，javafx吸收过来了，注意，样式之间要使用字符串拼接的方式，如果设置多次样式，新的会覆盖旧的

```java
button.setStyle("-fx-background-color: steelblue;"+
                "-fx-background-radius: 15;"+
                "-fx-text-fill: black;"+
                "-fx-font-size: 30"
               );
```

`设置单击事件`，参数ActionEvent，可以获取到当前按钮的对象

```java
button.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        System.out.println("点击了按钮");
        Button button = (Button)event.getSource();
        System.out.println("当前按钮的名字为："+button.getText());
    }
});
```

`双击事件`，可以使用过滤器，实现双击事件

```java
// 双击事件,也就是个过滤器,只有鼠标的操作，里面的方法才会执行
button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent event) {
        // 鼠标单击次数
        int count = event.getClickCount();
        if (count == 2){
            System.out.println("双击事件触发");
        }
    }
});
```

`获取到到底是鼠标哪个键点击了`:`event.getButton().name()`，因此，可以针对不同的按键进行处理，当然这里的`equals`可以用自带的成员名，也可以自己设置字符串

```java
// 这里只有鼠标左键单击两次才会触发事件
button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent event) {
        String name = event.getButton().name();// 按键名称
        int count = event.getClickCount();// 鼠标单击次数
        // 左键按两次才会触发
        if (count == 2 && name.equals(MouseButton.PRIMARY.name())){
            System.out.println("左键双击事件触发");
        }
    }
});
```

`键盘按键`：

```java
// 键盘按键按下
button.setOnKeyPressed(new EventHandler<KeyEvent>() {
    @Override
    public void handle(KeyEvent event) {
        String name = event.getCode().name();
        System.out.println("按下按键："+name);
    }
});

// 键盘按键释放
button.setOnKeyReleased(new EventHandler<KeyEvent>() {
    @Override
    public void handle(KeyEvent event) {
        String name = event.getCode().name();
        System.out.println("释放按键："+name);
    }
});
```

`设置快捷键`：

```java
// 方法3，这个没有问题
KeyCombination kcc = new KeyCodeCombination(KeyCode.A,KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN);
scene.getAccelerators().put(kcc, new Runnable() {
    @Override
    public void run() {
        System.out.println("按键");
    }        
});  
```

#### 1.2 多选按钮组

`CheckBox`是多选按钮，一共有三种状态：

选中；不选中；不确定(说实话没啥用)

#### 1.3 单选按钮组

`RadioButton`，单选按钮组

### 2 文本输入框

#### 2.1 单行文本

从英文`Filed`来看，应该叫`文本域`

```java
// 创建一个文本域
TextField textField = new TextField();
// 设置字体大小
textField.setFont(Font.font(30));
```

`行内文本`，非常常用的功能

```java
// 行内提示
textField.setPromptText("账号");
textField.setFocusTraversable(false);// 去掉焦点，这样才能显示出来
```

`监听内容`，可以在这里限制输入的文本内容

```java
// 监听
textField.textProperty().addListener(new ChangeListener<String>() {
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        // 如果超过七个字就设置为前七个字
        if (newValue.length()>7){
            textField.setText(oldValue);
        }
    }
});
```

`监听选择的内容`：可以监听文本框鼠标右键选择了那些内容

```java
// 监听选择了哪些内容
textField.selectedTextProperty().addListener(new ChangeListener<String>() {
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        System.out.println(newValue);
    }
});
```

`单机事件（回车）`:这个可以用来：按回车就获取下一个输入框的焦点，用法也很普遍

```java
// 文本框单机事件：其实是回车
passwordField.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        System.out.println(event);
    }
});
```

`密码文本框`，一样可以监听之类的

```java
PasswordField passwordField = new PasswordField();
```

#### 2.2 多行文本

`TextArea`：英文`Area`的意思是区，因此是多行文本

#### 2.3 label标签

`label标签`，无法输入，选中，只展示的文字

```java
Label label = new Label("这是标签");
```



### 5 弹出提示

一般的组件都可以使用这个

```java
// 弹出提示,这个是有个指针悬停时间的，超过大概快1秒才会出来
Tooltip tooltip = new Tooltip("这是tip");
tooltip.setFont(Font.font(30));
textField.setTooltip(tooltip);
```

### 6 超链接

`Hyperlink`:类似html的a,但是没有自带的点击事件，需要自己设置,默认带个边框挺丑的，可以css去掉

```java
Hyperlink hyperlink = new Hyperlink("www.baidu.com");
// 设置点击事件
hyperlink.setOnAction(event -> {
    // 通过HostServices对象访问浏览器此界面
    HostServices host = HyperLinkTest.this.getHostServices();
    host.showDocument(hyperlink.getText());
});
```

### 7 菜单

1. `menubar`：就是顶部的一行，里面是一个个的menu，
2. `menu`：就是一个个的按钮,
3. `menuitem`：每个menu打开后的子组件，

4. `CustomMenuItem`：在构造方法中可以放入任意的node，因此可以通过它来让菜单的item中展示任何东西

```
CustomMenuItem item = new CustomMenuItem(button);
```

5. `menuButton`：	

### 8 分割线

可以加到菜单中实现按钮的分隔，只需要在menu加item的时候把这个加上去就可以了

```java
SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();
```

### 9 可折叠组件

`titlePane`，其中可设置很多的子node，比如加入VBox，能做出类似web的左侧菜单

```java
Label label = new Label("这是标签");

// 可打开关闭的面板
TitledPane titlePane = new TitledPane("这是titlePane",label);

// 默认是展开的，可以设置默认关闭
titlePane.setExpanded(false);

// 可以加入类似web的icon图标
titlePane.setGraphic(图片的node);
```



## 四 布局



### 1 锚点布局

`AnchorPane(锚点)`：不同于group，AnchorPane更像是html的`div`,可以对子组件进行布局控制，AnchorPane跟group一样，也是需要放到`scene`的

```java
Button button = new Button("这是按钮");
button.setStyle("-fx-font-size: 30");
// 布局容器
AnchorPane anchorPane = new AnchorPane();
AnchorPane.setTopAnchor(button,100.0);
AnchorPane.setLeftAnchor(button,100.0);
AnchorPane.setRightAnchor(button,100.0);
AnchorPane.setBottomAnchor(button,100.0);
```

可以看到，就像是设置了`内边距`似的效果:

<img src="http://md.yangcc.top/image-20210128215738119.png" alt="image-20210128215738119" style="zoom: 25%;" />

但其实如果设置内边距的话：没有任何效果，还是和html区别很大的

```java
// 只设置内边距没有任何效果
anchorPane.setStyle("-fx-padding: 100;");

// 如果加上setTopAnchor就会有效果，即便是0.0
AnchorPane.setTopAnchor(button,0.0);
```

<img src="http://md.yangcc.top/image-20210128222553897.png" alt="image-20210128222553897" style="zoom: 25%;" />



总体来说这个组件可以做: 响应式的布局，因为是设置子组件与父组件的距离的，这样无论屏幕如何变化，相对位置是不变的

### 2 水平布局/垂直

`HBox`：可以让子组件水平的排列：这个如果组件超过Hbox的宽度是不会自动换行的，会几个组件平分这个宽度，然后这个组件是可以设置内边距，外边距的，可以使用java代码方式，也可以使用style

`VBox`：完全一样不多说

```java
// 两个按钮
Button button1 = new Button("这是按钮1");
button1.setStyle("-fx-font-size: 30");

Button button2 = new Button("这是按钮2");
button2.setStyle("-fx-font-size: 30");
// 水平布局
HBox hBox = new HBox();
hBox.getChildren().addAll(button1,button2);
hBox.setStyle("-fx-background-color: cadetblue");
hBox.setPrefSize(500,100);
```

可以看到无需设置，达到了水平的效果：

<img src="http://md.yangcc.top/image-20210128224150847.png" alt="image-20210128224150847" style="zoom:25%;" />

`设置内边距`：

```java
// java方法
hBox.setPadding(new Insets(10));
// style方式
hBox.setStyle("-fx-padding: 10");
```

`设置组件间的间距`：按钮挨的太近了，可以设置`spacing`

```java
// java方法
hBox.setSpacing(10);
// style方式
hBox.setStyle("-fx-spacing: 10;");
```

`设置外边距`：针对某个子组件，设置外边距

```java
// 设置外边距
HBox.setMargin(button1,new Insets(10));
```

`设置居中`：有很多样式，这个也可以用style，但是没有提示哈哈

```java
hBox.setAlignment(Pos.BASELINE_LEFT); // 水平居左
hBox.setAlignment(Pos.BASELINE_CENTER); // 水平居中
hBox.setAlignment(Pos.CENTER); // 水平垂直都居中
```

### 3 三种隐藏

```java
setManged(false); // 子组件消失不见，然后其他组件会覆盖他的位置
setVisible(false); // 子组件消失，其他组件不会覆盖位置
setOpacity(0); // 单纯的透明，还在那个位置，还可以点击等
```

### 4 方位布局

`BorderPane`：设置界面的五个部分

```java
// 方位布局
BorderPane borderPane = new BorderPane();
borderPane.setStyle("-fx-background-color: pink");

borderPane.setTop(anchorPane1);
borderPane.setBottom(anchorPane2);
borderPane.setLeft(anchorPane3);
borderPane.setRight(anchorPane4);
borderPane.setCenter(anchorPane5);
```

可以不设置每个部分，然后中间的Center会自动占据剩余的位置，也可以为每个部分继续使用这个进行布局，很方便

<img src="http://md.yangcc.top/image-20210129154200968.png" alt="image-20210129154200968" style="zoom:25%;" />

### 5  流式布局

`flowPane`的特点就是子组件可以根据窗口的大小自动换行，其他跟水平，垂直完全一样

```java
FlowPane flowPane = new FlowPane();
```

### 6 网格布局

`GridPane`：网格布局，给每个组件分配固定的位置

```java
Button button1 = new Button("button1");
Button button2 = new Button("button2");
Button button3 = new Button("button3");
// 网格布局
GridPane gridPane = new GridPane();
gridPane.setStyle("-fx-background-color: pink");
// 后面的参数是：列，行
gridPane.add(button1,0,0);
gridPane.add(button2,1,1);
gridPane.add(button3,3,1);
```

### 7 堆叠布局

`StackPane`：堆叠布局就像堆书一样，最后放的会在最上面，然后展示出来的话就是最后一个在最前面

```java
StackPane stackPane = new StackPane();
stackPane.setStyle("-fx-background-color: pink");
```

### 8 文本流式布局

`TextFlow`：文本流式布局，如果一行超过窗口大小，就会挤压到下一行

```java
// 文本
Text text = new Text("一段文字，说明了这是一个Text组件，这个组件是专门放文字的");

// 文本流式布局
TextFlow textFlow = new TextFlow();
textFlow.setStyle("-fx-background-color: pink;"+"-fx-font-size: 30");
textFlow.getChildren().add(text);
```

### 9 瓦片布局

`TilePane`：一家人就是要整整齐齐的，设置一个子组件的样式，对于其余的组件，都会变为同样的样式

```java
 TilePane tilePane = new TilePane();
```

### 10对话框

`DialogPane`：封装了下对话框的基本样式，太复杂，后面有封装这个的，他儿子`Dialog`之类的更简单

`创建对话框`，需要新建一个stage，scene的，

```java
// 点击弹出对话框
Button button = new Button("点击弹出窗口");

// 根节点
AnchorPane anchorPane = new AnchorPane();
anchorPane.setStyle("-fx-background-color: pink;"+"-fx-font-size: 30;");
anchorPane.getChildren().add(button);
AnchorPane.setTopAnchor(button,100.0);
AnchorPane.setLeftAnchor(button,100.0);

// 点击事件
button.setOnAction((event)->{
    // 弹出框
    DialogPane dialogPane = new DialogPane();
    dialogPane.setStyle("-fx-font-size: 30;");
    dialogPane.setHeaderText("这是Header");
    dialogPane.setContentText("这是content");

    // 按钮
    dialogPane.getButtonTypes().add(ButtonType.APPLY);
    dialogPane.getButtonTypes().add(ButtonType.CANCEL);

    // 获取按钮对象，这样就可以设置点击时间了
    Button apply = (Button) dialogPane.lookupButton(ButtonType.APPLY);
    Button close = (Button) dialogPane.lookupButton(ButtonType.CLOSE);


    // 一个新的stage来存放 dialogPane
    Scene scene = new Scene(dialogPane);
    Stage stage = new Stage();
    stage.setScene(scene);
}
```

对话框图片：

```java
// 右侧图片
ImageView imageView = new ImageView("/img/img.png");
dialogPane.setGraphic(imageView);
```

点击展示详细内容：有点意思

```java
// 点击展开内容
dialogPane.setExpandableContent(new Text("这是拓展内容"));
// 拓展内容默认展开
dialogPane.setExpanded(true);
```

<img src="http://md.yangcc.top/image-20210130162247432.png" alt="image-20210130162247432" style="zoom: 33%;" />

## 四 Bar



## 1 项目打包

以jar为例：

1. 在项目路径中点构建选-->jar包方式，然后选-->具有依赖的模块

<img src="http://md.yangcc.top/image-20210224220608793.png" alt="image-20210224220608793" style="zoom:50%;" />

2. 选择主类后，点确定即可有一个模版

<img src="http://md.yangcc.top/image-20210224220813601.png" alt="image-20210224220813601" style="zoom:50%;" />

3.点构建，选择编译

<img src="http://md.yangcc.top/image-20210224220929576.png" alt="image-20210224220929576" style="zoom:50%;" />

4. 选择刚才的jar模版即可
