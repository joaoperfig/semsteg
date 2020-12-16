# A WordNet Replacement Table
Providing a word replacement table for usage with semantic steganography systems.

The replacements.txt file provides a list of words and a set of replacemets (synonyms) for each word.
The set of replacements is deemed "safe", the listed replacements can replace the original word regardless of the context in which it appears.

## Construction
The original [WordNet](https://wordnet.princeton.edu/) synsets are expanded by inflecting some words using [Inflect](https://github.com/jaraco/inflect/blob/master/inflect.py) and [mlconjug](https://github.com/SekouD/mlconjug).
The "safe" set of replacements for a word is computed from the intersection of all synsets in which the word appears. This way it is ensured that the replacement is safe to be used regardless of the meaning that the original word took on.
