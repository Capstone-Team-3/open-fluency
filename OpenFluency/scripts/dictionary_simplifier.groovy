// Parse the Kanji dictionary and only print to the output file the lines that we need

def inputFile = "/Users/nicolastejera/Desktop/kanji_short.xml"
def outputFile = "/Users/nicolastejera/Desktop/kanji_simple.xml"


def out = new File(outputFile)

// Clear existing output file
if(out.exists()){ 
  out.delete() 
}

// Tags to replace
def replace = [
'meaning':'mn',
'reading':'rd',
'stroke_count':'sc',
'grade':'gr',
'cp_value':'uc',
'character':'ch'
]

// Tags to ignore
def ignore = ['literal','code','dic_','rad','nanori','cp_value', 'query_', 'meaning m_lang', 'q_code', 'jlpt', 'variant', 'reading r_type="korean_', 'rmgroup', 'misc','!--','reading_meaning']

// Tags to keep
def keep = ['cp_value cp_type="ucs"']

// Process input file
new File(inputFile).eachLine {
  def word = it.trim()

  boolean printIt = true

  ignore.each {
    if(word.startsWith("<$it") || word.startsWith("</$it")) {
      printIt = false
    }
  }

  keep.each {
    if(word.startsWith("<$it") || word.startsWith("</$it")) {
      printIt = true
    }
  }

  if(printIt){
    // here make the replacements
    replace.each {
      word = word.replace("<${it.key}", "<${it.value}").replace("</${it.key}", "</${it.value}")
    }

    out << word
  }
}