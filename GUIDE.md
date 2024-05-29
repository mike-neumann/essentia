# Guide
## Installation
1. Clone `essentia`
2. Configure JDK21 for project (Eclipse Temurin) and Language Level (SDK default)
3. Set Gradle Wrapper JDK Version to Project SDK
4. Reload Gradle and Wait until all background tasks have been completed (may take a while)
5. `publishToMavenLocal` using Gradle

## Usage
## Essentia Inject
When using `essentia-inject`, dependency management is no longer manual, every dependency will be managed automatically by essentia  
Heres a short breakdown:
1. You can use inject's default di container without any configuration to manage dependencies.  
But you can always implement your OWN DiContainer and tell essentia to use it, if you want to have more control of what happens to components within your application.
2. When running inject, essentia scans for classes annotated with `@Component`, and attempts to create a new instance of said class, when essentia detects, that no default constructor is available for that annotated class, essentia scans for constructors, which only have arguments, that are of types, that are also annotated with `@Component`
3. When inject detects said classes, it initializes them too, registering those to the DiContainer
4. In short: When using `essentia-inject` you can annotate your classes with `@Component` to have an instance of that class which you can pass around everywhere in your code.
5. Examples:
```
@Component
class MyComponent {
    // has no custom constructor, thus no dependencies...
}

```
```
@Component
class MyComponent2 {
    private final MyComponent myComponent;

    // this is a custom constructor, which defines a dependency on MyComponent, which is also a @Component
    // since it is also a @Component, just like the class we are currently in, essentia does all the work for you!
    // you DO NOT NEED TO CREATE A NEW INSTANCE OF THIS CLASS
    // inject does all that for you!!!
    public MyComponent2(MyComponent myComponent) {
        this.myComponent = myComponent:        
    }
}
```
```
@Component
class MyComponent3 {
    private final MyComponent myComponent;
    private final MyComponent2 myComponent2;

    // this custom constructor has TWO dependencies, both are a @Component, just like the class we are currently in
    // because of that, INJECT can do all the work for you and inject both dependencies.
    public MyComponent3(MyComponent myComponent, MyComponent2 myComponent2) {
        this.myComponent = myComponent;
        this.myComponent2 = myComponent2;
    }
}
```

Missing component annotation
```
class MyComponent4 {
    private MyComponent myComponent;

    // this custom constructor also has a dependency
    // but look at the class signature we have in the current class
    // WE HAVE NOT ANNOTATED THIS CLASS AS A COMPONENT
    // THUS INJECT WILL IGNORE THIS CLASS AND WONT REGISTER IT
    // THIS CLASS CANNOT BE USED AS A DEPENDENCY SINCE IT IS NOT MARKED AS A @Component
    public MyComponent4(MyComponent myComponent) {
        this.myComponent = myComponent;
    }
}
```

*Also be aware of circular dependencies, you cannot create a circular dependency, e.g:*  

`MyComponent -> MyComponent2 AND MyComponent2 -> MyComponent` (X)  

This will fail in an endless loop within `essentia-inject` and will warn you of a circular dependency between two components...  

NOTE: You can define as many components and as many dependencies as you wish, there is no limit on how many `essentia-inject` can handle

*ANY CLASS CAN BE A @Component*!!!!!!

---

## Essentia Configure
When using Essentia, you can implement easy object based configs within your project.  
Those configs can read a config file and represent its state using a complex object mapping system.  
It goes as follows:
```
@ConfigInfo(name = "myconfig.yml", processor = YMLFileProcessor.class) // here we define our config name / location where it is stored, as well as the processor implementation we want to use for this configuration file. (ALWAYS DOUBLE CHECK THE FILE PROCESSOR IF SOMETHING ISNT WORKING)
public class MyConfig extends Config {
    // now we can define our config properties as variables on this class.
    // PLEASE NOTE THAT THESE VARIABLE _MUST_ BE public OR ELSE ESSENTIA CANNOT REFERENCE THEM!!!

    // here we define the type that we want to save into the config.
    // this makes sure that the mapping for this type is working fine.
    @Property(String.class)
    public String someString = "";

    // you can also specify YOUR OWN type to save in the config
    // PLEASE NOTE THOUGH, THAT THIS TYPE _MUST_ HAVE A _DEFAULT CONSTRUCTOR_ OR ELSE ESSENTIA CANNOT REFERENCE ITS TYPE!!!
    @Property(MyOwnClass.class)
    public MyOwnClass myOwnClass = null;
}
```
```
public class MyOwnClass {
    // the properties on this complex type we are trying to save within the config must also be public and marked with @Property(...)
    @Property(String.class)
    public String someValueInComplex = null;
}
```
Once we defined our config, we can create an instance of that config, which will then load the file into the class object we created...  
`MyConfig myConfig = new MyConfig();`

To manipulate the config, we just change the values of the properties within this class' instance and call the `save()` method on it...  
`myConfig.someString = "test";`  
`myConfig.save();`
Now the config is saved to disc.

NOTE: You can also annotate this class with `@Component` if you are using `essentia-inject` (ANY CLASS CAN BE A @Component!!!!!!!!!!)
