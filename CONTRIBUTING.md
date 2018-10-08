# Essential Scala Collaborators

Congratulations! You have been selected as a content owner for the  **Scala Essentials** nanodegree.


## About the nanodegree

The **Scala Essentials** nanodegree aims to provide a comprehensive tour of the Scala Programming Language for begginers. 

You can see the design document [here](https://docs.google.com/document/d/1HZ1Rsn46x_BVAo8scSyVr9r5IesyTyHhGptjW1n_T6A/edit?usp=sharing).

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
* Mauricio Martín Saavedra Contreras
* Rafael Avila 
* Rodrigo Hernández Mota
* Oscar Vargar Torres

## About the content

A module should have content for two different situtations:
* **Documentation:** is a markdown file that contains the source knowledge, material, and resources needed to develop and present the module.
* **Presentation:** is a markdown file that contains the presentation material used for the module slides. The slides are automatically created using [Pandoc](https://pandoc.org/) for the [Reavel.js presentation framework](https://revealjs.com/#/).

The source content for each module in the markdown language is going to be safetly stored in a github repository. [Github pages](https://pages.github.com/) is used to publish the documentation and slides. 

The particular content for each module can be found in the agenda of [the design document](https://docs.google.com/document/d/1HZ1Rsn46x_BVAo8scSyVr9r5IesyTyHhGptjW1n_T6A/edit?usp=sharing). 

## Requirements

**Note:** Collaborators running on Windows 10 are encoraged to try the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) (WSL). 

As a content owner, you'll need the following tools in order to successfuly contribute to the project: 

* The [git](https://git-scm.com/) open source distributed version control system.
* A [github](http://github.com) account and access to the [Intersys Consulting](https://github.com/intersysconsulting) organization on the platform.
* At least Java 8 and the Scala Build Tool [SBT](https://www.scala-sbt.org/1.x/docs/Setup.html).
* The [Jekyll](https://jekyllrb.com/) site generator. 
* [Pandoc](https://pandoc.org/), the file converter tool. 

A setup script on a **debian-based OS** should look similar to:
```bash
#!/bin/bash

# Install git
sudo apt install git

# Install java 8
sudo apt install openjdk-8-jdk

# Install SBT
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
sudo apt update
sudo apt install sbt
sbt about

# Install Ruby and Jekyll
sudo apt install ruby ruby-dev build-essential
export GEM_HOME=$HOME/gems
export PATH=$HOME/gems/bin:$PATH
gem install jekyll bundler

# Install pandoc
sudo apt install pandoc
```

## Github Repository

The nanodegree's content and learning material will be hosted in [this github repository](). 

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
        |        |- js/...
        |        |- img/...
        |- tut/
            |- docs/
            |    |- index.md
            |    |- module1.md
            |    |- module2.md
            |    |- module3.md
            |    |- ...
            |- slides/
            |    |- index.md
            |    |- module1.md
            |    |- module2.md
            |    |- module3.md
            |    |- ...
            |- index.md
            |- ...
            
```
## Development

The development of each module should occur on a separate and individual branch (e.g. `feat/module1`, `feat/module7`). When finish the development, please create a PR onto the `develop` branch. 

## Publishing

Once your module is complete (docs + slides) you can see the site either in local mode or by publishing into github pages. 

### Local mode

See the full site in local mode with: `./publish.sh --local`

This will publish the site at: `http://localhost:4000/scala-essentials/`

### Github pages
Only the content on the develop/master branch should be published to github pages with: `./publish.sh --site`

##