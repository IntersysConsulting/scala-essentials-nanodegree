# Essential Scala Collaborators

Congratulations! You have been selected as a content owner for the  **Scala Essentials** nanodegree.


## About the nanodegree

The **Scala Essentials** nanodegree aims to provide a comprehensive tour of the Scala Programming Language for begginers. 

You can see a complete definition of the Syllabus [here](https://intersysconsulting.github.io/scala-essentials-nanodegree/).

**Modules**:
1. Introduction
2. Essentials
3. Objects and classes
4. Functions
5. Traits for data modelling
6. Sequencing computation
7. Idiomatic Scala
8. Type classes
9. Functional programming
10. Popular frameworks and toolkits

## About content owners

As a content owner your job is to design and create the content of at least one module. You can build a small team with other content owners to assist on the content creation. Please consider that each conent owner has full responsibility of their own modules. 

Content owners:
* [Mauricio Martín Saavedra Contreras](https://gitter.im/MMSaavedraC)
* [Rafael Avila]()
* [Rodrigo Hernández Mota](https://gitter.im/rhdzmota)
* [Oscar Vargas Torres](https://gitter.im/oscarvarto)

## About the content

A module should have content for two different situtations:
* **Documentation:** is a markdown file that contains the source knowledge, material, and resources needed to develop and present the module.
* **Presentation:** is a markdown file that contains the presentation material used for the module slides. The slides are automatically created using [Pandoc](https://pandoc.org/) for the [Reavel.js presentation framework](https://revealjs.com/#/).

The source content for each module in the markdown language is going to be safetly stored in a github repository. [Github pages](https://pages.github.com/) is used to publish the documentation and slides. 

The particular content for each module can be found in the agenda of [the official Syllabus](https://intersysconsulting.github.io/scala-essentials-nanodegree/). 

## Requirements

**Note:** Collaborators running on Windows 10 are encoraged to try the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) (WSL). 

As a content owner, you'll need the following tools in order to successfuly contribute to the project: 

* The [git](https://git-scm.com/) open source distributed version control system.
* A [github](http://github.com) account and access to the [Intersys Consulting](https://github.com/intersysconsulting) organization on the platform.
* Java V8 or newer and the Scala Build Tool [SBT](https://www.scala-sbt.org/1.x/docs/Setup.html).
* The [Jekyll](https://jekyllrb.com/) site generator. 
* [Pandoc](https://pandoc.org/), the file converter tool. 

See the `setup/debian-based.sh` file for a **debian-based OS** setup script example. 

## Github Repository

The nanodegree's content and learning material will be hosted in [this github repository](https://github.com/IntersysConsulting/scala-essentials-nanodegree). 

Consider the following relevant dirs and docs:
* `publish.sh` is a bash script used to publish the content microsite.
* `src/main/tut/` contains the markdown material of the microsite
    * `index.md` is the landing page of the microsite.
    * `authors.md` 
    * `docs/` contains the markdown resources of the modules.
        * `moduleX.md` is the content of the module X.
* `slides/` contains the makdown resources of the slides.
    * `moduleX.md` is the content of the module X. 

**Repository structure**:

```text
|- README.md
|- LICENSE.md
|- .gitignore
|- publish.sh
|- build.sbt
|- project/...
|- slides/
    |- module1.md
    |- module2.md
    |- ...
|- src/
    |- main/
        |- scala/
        |    |- com.intersysconsulting.nanodegrees.scalaessentials
        |        |- examples/
        |        |    |- module1/...
        |        |    |- module2/...
        |        |    |- ...
        |        |- challenge/...
        |- resources/
        |    |- microsite/
        |        |- data/
        |        |    |- menu.yml
        |        |- slides/...
        |        |- js/...
        |        |- img/...
        |- tut/
            |- docs/
            |    |- index.md
            |    |- module1.md
            |    |- module2.md
            |    |- module3.md
            |    |- ...
            |- index.md
            |- docs.md
            |- slides.md
            
```
## Development

Content development should follow the [gitflow](https://datasift.github.io/gitflow/IntroducingGitFlow.html) guideline:

* `master` : branch that contains the stable version of the nanodegree.
* `develop` : branch that contains the current work (development). 
* `feature/x` : branch created from `develop` that implements a new feature `x` where `x` is the name of the feature using the **kebab case** naming convention (e.g. `feature/this-is-feat-x`). 
* `bugfix/x` : branch created from `develop` that fixes a given bug `x` following the **kebab case** naming convention.
* `hotfix/x` : branch created from `master` that fixes an urgent matter `x` following the **kebab case** naming convention. This branch should be merged into `master` and `develop` when approved. 
 
### Content creation

Consider the following guideline:

1. Pull the latest copy of the `develop` branch.
2. Create your own `feature/moduleX` branch (e.g. `feature/module3`, `feature/module5`).
3. Add the content and slides at `src/main/tut/docs/moduleX.md` and `slides/moduleX.md`. You can add working code examples/exercies in `src/main/scala/...`. 
4. Commit your work into your branch and test your content. Push to the remote. 
5. Create a PR of your branch with `develop` and solve any conflicts and reviews.

## Publishing

Once your module is complete (docs + slides) you can see the site either in local mode or by publishing into github pages. 

### Local mode

See the full site in local mode with: `./publish.sh --local`

This will publish the site at: `http://localhost:4000/scala-essentials/`

### Github pages
Only the content on the `master` branch should be published to github pages with: `./publish.sh --site`

##
