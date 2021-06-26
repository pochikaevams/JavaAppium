package tests;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import lib.CoreTestCase;
import lib.Platform;
import org.junit.Assert;
import org.junit.Test;
import ui.*;
import ui.factories.ArticlePageObjectFactory;
import ui.factories.MyListPageObjectFactory;
import ui.factories.NavigationUIPageObjectFactory;
import ui.factories.SearchPageObjectFactory;

@Epic("Тесты для моих списков")
public class MyListsTests extends CoreTestCase {

    private static final String folderName = "Folder";
    private static final String login = "Himmerka";
    private static final String password = "j,tl21#ytn";

    @Test
    public void testAddAndDeleteArticleToListForMW() throws IllegalAccessException {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        ArticlePageObject ArticlePageObject;
        ArticlePageObject = ArticlePageObjectFactory.get(driver);
        NavigationUIPageObject NavigationUIPageObject = NavigationUIPageObjectFactory.get(driver);
        MyListsPageObject MyListsPageObject = MyListPageObjectFactory.get(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        String firstArticleTitleOnSearch = "Indonesian island";
        SearchPageObject.goToTitle(firstArticleTitleOnSearch);
        ArticlePageObject.addArticleToFavoriteList();
        if (Platform.getInstance().isMW()) {
            AuthorizationPageObject Auth = new AuthorizationPageObject(driver);
            Auth.clickAuthButton();
            Auth.enterLoginData(login, password);
            Auth.submitForm();
            MyListsPageObject.waitForTitle(firstArticleTitleOnSearch);
            ArticlePageObject.addArticleToFavoriteList();
        }

        if (Platform.getInstance().isAndroid()) {
            ArticlePageObject.confirmArticleSelection();
            ArticlePageObject.clearDefaultNameOfFolder();
            ArticlePageObject.giveArticleNewName(folderName);
            ArticlePageObject.confirmArticleAddiction();
            NavigationUIPageObject.exitFromArticlePage();
        }
        SearchPageObject.initSearchInput();
        if (Platform.getInstance().isAndroid() || Platform.getInstance().isMW()) {
            SearchPageObject.typeSearchLine("Java");
        }
        String secondArticleTitleOnSearch = "Object-oriented programming language";
        SearchPageObject.goToTitle(secondArticleTitleOnSearch);
        ArticlePageObject.addArticleToFavoriteList();
        NavigationUIPageObject.openNavigation();
        NavigationUIPageObject.clickMyLists();
        MyListsPageObject.swipeByArticleToDeleteForMW();

        if (Platform.getInstance().isAndroid()) {
            ArticlePageObject.addArticleToExistingFolder(folderName);
        }
        NavigationUIPageObject.exitFromArticlePage();
        NavigationUIPageObject.goToSavedFolders();
        if (Platform.getInstance().isAndroid()) {
            MyListsPageObject.goToSelectedFolder();
            String firstArticleTitle = "object-oriented programming language";
            String secondArticleTitle = "Indonesian island";
            MyListsPageObject.deleteArticle(firstArticleTitle);
            MyListsPageObject.waitForTitle(secondArticleTitle);
            MyListsPageObject.waitForTitleGone(firstArticleTitle);
        } else if (Platform.getInstance().isIOS()) {
            MyListsPageObject.swipeToDeleteArticleForIOS();
            MyListsPageObject.waitNotForTypeImage();
        } else {
            SearchPageObject.clickByArticleWithSubstringForMW();
            ArticlePageObject.waitForTitleElement();
            String first_element_in_list = ArticlePageObject.getArticleTitle();
            Assert.assertEquals(
                    "Cannot presented this element",
                    secondArticleTitleOnSearch,
                    first_element_in_list
            );
        }
    }

    @Test
    @Features(value={@Feature(value = "Search"), @Feature(value = "Article"),@Feature(value = "Navigation")})
    @DisplayName("Сохранение двух статей и удаление одной из списка")
    @Description("Добавление статей и затем удаление второй статьи")
    @Step("Начало теста")
    @Severity(SeverityLevel.BLOCKER)
    public void testAddAndDeleteArticleToListForiOS() throws IllegalAccessException {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        ArticlePageObject ArticlePageObject = ArticlePageObjectFactory.get(driver);
        NavigationUIPageObject NavigationUIPageObject = NavigationUIPageObjectFactory.get(driver);
        MyListsPageObject MyListsPageObject = MyListPageObjectFactory.get(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        String firstArticleTitleOnSearch = "Island of Indonesia";
        SearchPageObject.goToTitle(firstArticleTitleOnSearch);
        ArticlePageObject.addArticleToFavoriteList();
        ArticlePageObject.confirmArticleSelection();
        if (Platform.getInstance().isIOS()) {
            ArticlePageObject.clearDefaultNameOfFolder();
            ArticlePageObject.giveArticleNewName(folderName);
            ArticlePageObject.confirmArticleAddiction();
        }
        NavigationUIPageObject.exitFromArticlePage();
        SearchPageObject.initSearchInput();
        if (Platform.getInstance().isAndroid()) {
            SearchPageObject.typeSearchLine("Java");
        }
        String secondArticleTitleOnSearch = "Object-oriented programming language";
        SearchPageObject.goToTitle(secondArticleTitleOnSearch);
        ArticlePageObject.addArticleToFavoriteList();
        if (Platform.getInstance().isAndroid()) {
            ArticlePageObject.addArticleToExistingFolder(folderName);
        }
        NavigationUIPageObject.exitFromArticlePage();
        NavigationUIPageObject.goToSavedFolders();
        if (Platform.getInstance().isAndroid()) {
            MyListsPageObject.goToSelectedFolder();
            String firstArticleTitle = "object-oriented programming language";
            String secondArticleTitle = "island of Indonesia";
            MyListsPageObject.deleteArticle(firstArticleTitle);
            MyListsPageObject.waitForTitle(secondArticleTitle);
            MyListsPageObject.waitForTitleGone(firstArticleTitle);
        }
        MyListsPageObject.swipeToDeleteArticleForIOS();
        MyListsPageObject.waitNotForTypeImage();
    }

    @Test
    @DisplayName("Проверка экрана входа")
    @Description("Welcome экран")
    @Step("Начало теста")
    @Severity(SeverityLevel.NORMAL)
    public void testWelcomeScreen() throws IllegalAccessException {

        if (Platform.getInstance().isAndroid()){
            WelcomePageObject WelcomePage = new WelcomePageObject((AppiumDriver) driver);
            WelcomePage.waitForLearnMoreLink();
            WelcomePage.waitAndClickNextButton();
            WelcomePage.waitForNewWaysToExplore();
            WelcomePage.waitAndClickNextButton();
            WelcomePage.waitForSearchInNearly300Languagese();
            WelcomePage.waitAndClickNextButton();
            WelcomePage.waitForLearnMoreAboutDataCollected();
            WelcomePage.waitAndClickForGetStarted();

        } else {
            return;
        }
    }

    @Test
    @Features(value={@Feature(value = "Search")})
    @DisplayName("Проверка трех заголовков и описания в выдаче поиска")
    @Description("Проверка соответствия строки поиска и заговков/описания")
    @Step("Начало теста")
    @Severity(SeverityLevel.MINOR)
    public void testDescriptionAndTitleSearch() throws Exception {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Jamaica");
        String t01 = "Jamaica, Queens";
        String d1 = "Neighborhood of Queens, New York City";
        SearchPageObject.waitForElementByTitleAndDescription(t01, d1);

        String t02 = "Jamaica";
        String d2 = "Island sovereign state in the Caribbean Sea";
        SearchPageObject.waitForElementByTitleAndDescription(t02, d2);

        String t03 = "Jamaica, Queens";
        String d3 = "Neighborhood of Queens, New York City";
        SearchPageObject.waitForElementByTitleAndDescription(t03, d3);
    }
}

