import os
import glob
import matplotlib.pyplot as plt


def readfile(file):
    f = open(file, "r")
    text = f.read()
    f.close()
    return text

def f(alphabet, rep):
    return 1 - ((1-(1/alphabet))**rep)

def dovals():
    #directory = "../data/cohacorpus_stats/"
    #directory = "../data/globwbe_stats/"
    directory = "../data/allstats/"
    owd = os.getcwd()
    os.chdir(directory)
    files = glob.glob("*.txt")
    os.chdir(owd)
    
    vs = []
    for file in files:
        print(file)
        content = readfile(directory+file)
        vals = content.split(",")   
        for val in vals:
            if val != "":
                n = eval(val)
                vs += [n]
    return vs
    

def doprobs(section_size, alphabet, vals):    
    sections = []
    current = []
   
    for n in vals:
            if (len(current) < section_size):
                current += [n]
            else:
                sections += [current]
                current = []
                    
    reps = []
    for section in sections:
        rep = 1
        for word in section:
            rep = rep*word
        reps += [rep]
        
    probs = []
    for rep in reps:
        probs += [f(alphabet, rep)]
    return probs

def main(section_num, probs):
        
    messages = []
    current = []
    for prob in probs:
        if(len(current)<section_num):
            current += [prob]
        else:
            messages += [current]
            current = []
            
    messageprobs = []
    for message in messages:
        total = 1
        for prob in message:
            total *= prob
        messageprobs += [total]
        
    total = 0
    for messageprob in messageprobs:
        total += messageprob
        
    return total / len(messageprobs)

vals = dovals()
#probs = doprobs(200, 29, vals)
#x = list(range(1, 51))
#y = []
#stri = ""
#for i in x:
#    v = main(i, probs)
#    stri += "("+str(i)+","+str(v*100)+")"
#    y += [v]
x = list(range(20, 501, 10))
y = []
stri = ""
for i in x:
    probs = doprobs(i*6, 32**6, vals)
    v = main(12/6, probs)
    print(i, "->", v*100, "%")
    #sectionsize = i*6.2150626
    #rate = 1/sectionsize    
    stri = stri + "("+str(i)+","+str(v*100)+")" 
    y+= [v]

print(stri)
plt.plot(x, y)
plt.show()