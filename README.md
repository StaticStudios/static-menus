# Static Menus
<p>
  <a href="https://repo.staticstudios.net/#/releases/net/staticstudios/menus">
    <img src="https://repo.staticstudios.net/api/badge/latest/releases/net/staticstudios/menus?color=9ec3ff&name=Maven">
  </a>
  <a href="https://github.com/StaticStudios/Static-Menus">
    <img src="https://img.shields.io/github/actions/workflow/status/StaticStudios/static-menus/publish.yml?branch=master&logo=github">
  </a>
</p>
Static Menus is a custom invenetory library made for Paper servers. 

## How to use it
To use the libray in your project you need to add the StaticStudios repository and declare the dependency.

```gradle
repositories {
  maven {
      name "StaticStudios"
      url "https://repo.staticstudios.net/releases"
  }
}

dependencies {
  implementation "net.staticstudios:menus:1.0.5"
}
```
