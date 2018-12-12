import os
import sys


if __name__ == "__main__":
    ssns = list([s.strip() for s in input("Enter ssns comma delimited:\n").split(",")])
    for s in ssns:
        os.system('"C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" https://web.njit.edu/~lrc22/webapps8/MyAccount?ssn={}'.format(s))
