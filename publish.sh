#!/bin/bash

makeSlides () {
  while read -r slide
  do
	module="$(basename $slide .md)"
	echo "Generating reveal.js slide for: $module"
	pandoc --from markdown+tex_math_single_backslash+inline_code_attributes --standalone --mathjax --variable colorlinks=true --to revealjs --output target/site/slides/${module}.html -V revealjs-url=http://lab.hakim.se/reveal-js -V theme:night src/main/tut/slides/${module}.md
  done < <(find src/main/tut/slides -name "module*")	
}

while [ ! $# -eq 0 ]
do
	case "$1" in
		--help | -h)
			echo "publish.sh [OPTIONS]"
			echo "  -h --help  : displays the help menu."
			echo "  -l --local : builds the microsite and runs the local service with jekyll."
			echo "  -s --site  : builds the microsite and publish the site into Github pages." 
			exit
			;;
		--local | -l)
			echo "Local mode..."
			rm -rf target/
			rm -rf _site
            		sbt makeMicrosite
			makeSlides
			jekyll serve -s target/site
			exit
			;;
		--site | -s)
			echo "Publishing microsite procedure..."
			rm -rf target/
			sbt makeMicrosite
			makeSlides
			sbt ghpagesPushSite
			exit
			;;
	esac
	shift
done

