# Fabric Mod Template

---

An easy-to-use Minecraft mod template for mods that are built on the Fabric mod loader.

This template follows the **DRY** principle (Don't Repeat Yourself). This means that you can change any of your mod's details in the gradle.properties file and have those changes be automatically reflected in your fabric.mod.json.

Lastly, for those of you who use GitHub Actions, it comes with a [super basic workflow file](https://github.com/Ayydxn/FabricModTemplate/blob/master/.github/workflows/build.yml) that will build your mod on every pull request and push to your repository.

---

## ❗Reporting Issues

If you come across any bugs/issues while using the template, you can report them using the [issue tracker](https://github.com/Ayydxn/FabricModTemplate/issues). Before opening an issue, please use the search tool to make sure that your issue hasn't already been reported. As soon as an issue is found to be a duplicate, it will be closed immediately.

Please note that while the issue tracker is also open to feature requests such as adding support for another mod loader.

---

## 🛠️ Usage

### 📃 Requirements

- A JDK (Java Development Kit) for Java 21.
    - I recommend using the [Azul Zulu]() distribution as this is what the template is built with. However, any JDK distribution should work perfectly fine.

- Any Java IDE such as [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/ide/). You may also use other code editors with support for Java and Gradle such as [Visual Studio Code](https://code.visualstudio.com/).
    - If you're new to modding, I recommend using IntelliJ IDEA as it is what is used by most people for modding. It is also what is used in most modding tutorials.

After cloning the project, you'll want to open it in your chosen code editor and wait for Gradle to load the project. Once the project has loaded, you can start modding!

If you want to change any of the details about your mod such as the name or version, you will want to do that in the gradle.properties file.

To build your mod, you can run the default `build` task and your mod will be built. The built JAR can be found in the `build/libs` folder.

---

## 📄 License

This template is available under the CC0 license. For more information, please read the license [here](https://choosealicense.com/licenses/cc0-1.0/#). 