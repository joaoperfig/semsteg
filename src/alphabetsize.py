
def f(alphabet, rep):
    return 1 - ((1-(1/alphabet))**rep)

def fmin(alphabet, prob):
    i = 1
    while True:
        if f(alphabet, i) > prob:
            return i
        i += 1
        
        
stri = ""
for i in range(1, 101, 2):
    print(i, fmin(i, 0.95))
    stri += "("+str(i)+","+str(fmin(i, 0.95))+")"
print (stri)