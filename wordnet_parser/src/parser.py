from os import listdir
from os.path import isfile, join
from io import open
import inflect
import mlconjug

conjugator = mlconjug.Conjugator(language='en')
infeng = inflect.engine()
datasrc = "../data"

def openfile(fpath):
    f = open(fpath, "r", encoding='utf-8')
    lines = f.readlines()
    f.close()
    return lines

def appendall(lista, rootstring):
    res = []
    for el in lista:
        res += [rootstring+el]
    return res    

def processline(line): 
    if (len(line) < 10) or (line[0] == " "):
        return "", []
    parts = line.split(" ")
    word = parts[0]
    parts = parts[1:]
    sets = []
    for part in parts:
        if (len(part)==8):
            sets += [part]
    return word, sets

def processlines(lines, safemode):
    mysynsets = {}
    for line in lines:
        word, synsets = processline(line)
        if safemode:
            safe = True
            for char in word:
                if not (char in "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"):
                    safe = False
            if safe and (word != ""):
                mysynsets[word] = synsets
        else:
            if (word != "") and not (("_" in word) or ("/" in word)):
                mysynsets[word] = synsets
    return mysynsets

def pluralize(noun):
    plural = infeng.plural_noun(noun)
    return plural

def conjugate(verb):
    conjugated = conjugator.conjugate(verb)
    conj_forms = conjugated.iterate()
    res = {}
    for form in conj_forms:
        if ("/" in form[3]):
            res[form[1]+"_"+form[2]] = form[3].split("/")[0]
        else:
            res[form[1]+"_"+form[2]] = form[3]
    return res    

def main(safemode):
    datafiles = [f for f in listdir(datasrc) if isfile(join(datasrc, f))]
    filedatas = {}
    for file in datafiles:
        print("Reading "+file)
        lines = openfile(join(datasrc, file))
        print("Parsing "+file)
        filedatas[file] = processlines(lines, safemode)
        
    #to join all, names of sets need to be changed to account for repeated synset names accross files
    for file in datafiles:
        root = file.split(".")[1]
        print(root)
        for word in filedatas[file]:
            filedatas[file][word] = appendall(filedatas[file][word], root)
            
    if safemode:
        datafiles += ["debug"]
        filedatas["debug"] = {}
        safewords = ["not", "who", "but", "me", "what"]
        for safeword in safewords:
            filedatas["debug"][safeword] = ["set"+safeword] 
          
    print("Joining equal sets")  
    #equal words get their sets joined
    word_sets = {}
    for file in datafiles:
        for word in filedatas[file]:
            if (word in word_sets):
                word_sets[word] += filedatas[file][word]
            else:
                word_sets[word] = filedatas[file][word]
                
    print("Inverting into synsets")  
    #invert into synsets
    set_words = {}
    for word in word_sets:
        for sset in word_sets[word]:
            if sset in set_words:
                set_words[sset] += [word]
            else:
                set_words[sset] = [word]
    
    print('word_sets["television"] ->', word_sets["television"])
    print('set_words["noun04413042"] ->',set_words["noun04413042"])
    
    print('word_sets["time"] ->', word_sets["time"])
    for word_set in word_sets["time"]:
        print('set_words["'+word_set+'"] ->',set_words[word_set])    
    
    print("Pluralizing nouns") 
    #pluralize nouns
    singular_sets = list(set_words)
    for sset in singular_sets:
        if "noun" in sset:
            pluralset = []
            for word in set_words[sset]:
                pluralset += [pluralize(word)]
            set_words["plural_"+sset] = pluralset
            
    print('set_words["plural_noun04413042"] ->',set_words["plural_noun04413042"])
    
    print('word_sets["drag"] ->', word_sets["drag"])
    print('set_words["verb01201647"] ->',set_words["verb01201647"])    
    
    print("Conjugating verbs") 
    #conjugate verbs
    uncojugateds = list(set_words)
    for sset in uncojugateds:
        if "verb" in sset:
            conjugateds = {}
            for word in set_words[sset]:
                conjugations = conjugate(word)
                for conjugation in conjugations:
                    setname = conjugation+"_"+sset
                    if setname in set_words:
                        set_words[setname] += [conjugations[conjugation]]
                    else:
                        set_words[setname] = [conjugations[conjugation]]
                        
    print('set_words["indicative present_3s_verb01201647"] ->',set_words["indicative present_3s_verb01201647"])    
    
    print("Removing duplicate words") 
    #remove duplicates
    for sset in set_words:
        set_words[sset] = list(dict.fromkeys(set_words[sset]))
        
    print("Inverting Synsets to Wordsets") 
    #invert synsets back into wordsets
    word_sets = {}
    for sset in set_words:
        for word in set_words[sset]:
            if word in word_sets:
                word_sets[word] += [sset]
            else:
                word_sets[word] = [sset]
    
    print('word_sets["tries"] ->', word_sets["tries"])
    print(len(word_sets))
    #create replacement sets
    replacements = {}
    for word in word_sets:
        setids = word_sets[word]
        sets = []
        for sset in setids:
            sets += [set_words[sset]]
        #print(word, sets)
        reps = []
        for rep in sets[0]:
            inall = True
            for sset in sets:
                if not (rep in sset):
                    inall = False
            if inall: # and (rep != word):
                reps += [rep]
        if (len(reps) > 1):
            replacements[word] = reps
            #print(word, reps)
        
    return replacements

if __name__ == "__main__":
    
    safemode = True
    
    res = main(safemode)
    
    print("saving results")
    f = open("parsed.txt", "w", encoding='utf-8')
    words = list(res)
    words = sorted(words) # sort end result
    for word in words:
        f.write(word + ",")
        for rep in res[word]:
            # avoid writing the word twice per line
            if rep != word:
                f.write(rep + ",")
        f.write("\n")
    f.close()
    
    sentence = "A dispute over the composition of a Surrey side chosen to play a high-profile game in 1909, after several professional players were omitted for disciplinary reasons, led to an increasingly bitter argument between Crawford and the Surrey authorities. Crawford was told he had no future with the club, and moved to Australia. There, he worked as a teacher and continued his cricket career with South Australia. "
    sentence = sentence.lower()
    words = sentence.split()
    for i in range(len(words)):
        try:
            words[i] = res[words[i]]
            print(res[words[i]])
        except:
            words[i] = [words[i]]
            print([words[i]])
            
    print(words)