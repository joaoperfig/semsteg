import os
import glob
import matplotlib.pyplot as plt


def readfile(file):
    f = open(file, "r")
    text = f.read()
    f.close()
    return text

def main():
    #directory = "../data/cohacorpus_stats/"
    #directory = "../data/globwbe_stats/"
    directory = "../data/allstats/"
    owd = os.getcwd()
    os.chdir(directory)
    files = glob.glob("*.txt")
    os.chdir(owd)
    total = 0
    replaceables = 0
    replaceables_after = 0
    lastreplaceable = False
    for file in files:
        print(file)
        content = readfile(directory+file)
        vals = content.split(",")
        for val in vals:
            if val != "":
                n = eval(val)
                if n == 1:
                    total += 1
                    lastreplaceable = False
                else:
                    total += 1
                    replaceables += 1
                    if lastreplaceable:
                        replaceables_after += 1
                    lastreplaceable = True
    
    print ("probability of replaceables ->", replaceables/total)
    print ("knowing last is replaceable ->", replaceables_after/replaceables)
    
    
    
main()