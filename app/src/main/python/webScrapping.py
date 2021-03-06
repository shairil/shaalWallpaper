import bs4
from bs4 import BeautifulSoup
import requests
from io import BytesIO
from PIL import Image, UnidentifiedImageError
from googlesearch import search


def init(url, n):
    global soup
    #url = "https://mobile.alphacoders.com/by-category/3?page=" + str(n)
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'lxml')
    response.close()


def getId(imgURL):
    # imgURL = getHighImgResolution(imgURL)
    imgURL = imgURL.replace("thumb-", "")
    j = imgURL.rfind('.')
    i = imgURL.rfind('/')
    return int(imgURL[i+1:j])


def getHighImgResolution(imgURL):
    return imgURL.replace("thumb-", "")
#    try:
#        image_raw = requests.get(img)
#        image = Image.open(BytesIO(image_raw.content))
#        return img
#
#    except UnidentifiedImageError:
#        return imgURL


def getCount():

    try:
        lastPage = soup.findAll('div', attrs={'class': 'active custom-nav-tabs-element'})
        s = 'active custom-nav-tabs-element">'
        end = str(lastPage[0]).index(' Phone')
        start = str(lastPage[0]).index(s)
        count = (int(str(lastPage[0])[start + len(s):end]))
        return count
    except Exception:
        return -1



def anime_wallpaper(url, n):
    url = url+str(n)
    init(url, n)
    movieList = soup.select('div.item a ')
    resolution = soup.select('div.item span')
    res = []

    for i in resolution:
        ind = str(i).index("resolution-info\">")
        res.append(str(i)[ind + 19:-7].strip())

    title = []
    for i in movieList:
        ind = str(i).index("title=")
        title.append(str(i)[ind + 7:-24])

    imgURL = []
    id = []
    for i in movieList:
        ind = str(i).index("src=")
        end = str(i).index("\" style")
        img = str(i)[ind + 5:end]
        imgURL.append(img)
        id.append(getId(img))

    return title, imgURL, res, id


def getTrueUrl(url):
    # url = "https://wall.alphacoders.com/tag/marin-kitagawa-wallpapers"
    response = requests.get(url)
    response.close()
    soup = bs4.BeautifulSoup(response.content, 'lxml')
    s = str(soup.select('div.related-content a.btn-mobile'))
    print(s)
    try:
        i = s.find("href")
        j = s.find("title")
        return s[i + 6:j - 2]
    except Exception:
        print('Error')
        return url


def searchGoogle(query, n):
    query = query + " mobile alphacoders phone wallpapers abyss"
    global i
    cnt = 1
    for j in search(query, tld="co.in", num=5, stop=5, pause=2):
        if cnt == 1:
            i = j

        cnt += 1
        if checkURL(j):
            i = j
            break
        elif j.find('https://wall.alphacoders.com/by_sub_category.php?id=') != -1:
            i = j
            break
        i = j

    url = check(i) + '?page='
    a = list(anime_wallpaper(url, n))
    a.append(url)
    return tuple(a)


def check(url):
    if url.find('https://mobile.alphacoders.com/') != -1:
        return url
    elif url.find('https://wall.alphacoders.com/tag/') != -1:
        return getTrueUrl(url)
    elif url.find('https://wall.alphacoders.com/by_sub_category.php?id=') != -1:
        j = len("https://wall.alphacoders.com/by_sub_category.php?id=")
        i = url.index('&')
        print(j, i)
        URL = "https://mobile.alphacoders.com/by-sub-category/" + url[j:i]
        return URL
        #return
    else:
        return url

def wrapper(n):
    url = 'https://mobile.alphacoders.com/by-category/3?page='
    return anime_wallpaper(url, n)

def wrapperGoogle(url, n):
    return anime_wallpaper(url, n)




def getGODWallpaper(n):
    url = "https://ghantee.com/lord-shiva-wallpapers/?_page=" + str(n)
    url = "https://ghantee.com/lord-shiva-wallpapers/?_page=" + str(n)
    init(url, 0)
    listWallpaper = soup.select("div.post-thumb-img-content img")
    print(listWallpaper)
    wallpapers = []

    for i in listWallpaper:
        i = str(i)
        print(i)
        end = i.index("1080w")
        start = i[0:end].rfind("https://")
        wallpapers.append(i[start:end].strip())
        # print(i)
    # print(listWallpaper)
    print(wallpapers)


def checkURL(url):
    if url.find("https://mobile.alphacoders.com/by-sub-category/") != -1 or url.find("https://mobile.alphacoders.com/by-tag/") != -1:
        return True
    return False