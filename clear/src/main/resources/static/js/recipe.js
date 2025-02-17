document.addEventListener('DOMContentLoaded', function () {
    console.log("JavaScript loaded successfully");

    // Функция для переключения вкладок
    window.showTab = function(type) {
        document.querySelectorAll('.tab-content').forEach(tab => tab.style.display = 'none');
        document.querySelectorAll('.tab-button').forEach(button => button.classList.remove('active'));

        let activeTab = document.getElementById(type);
        if (activeTab) {
            activeTab.style.display = 'block';
            document.querySelector(`.tab-button[data-type="${type}"]`).classList.add('active');
        }
    };

    // Открываем первую вкладку при загрузке
    const firstTab = document.querySelector('.tab-button');
    if (firstTab) {
        showTab(firstTab.getAttribute('data-type'));
    }

    // Функция для показа/скрытия рецепта
    window.toggleRecipe = function(recipeId) {
        console.log("Toggle recipe for:", recipeId);
        let recipeTable = document.getElementById(recipeId);

        if (!recipeTable) {
            console.error("Recipe table not found:", recipeId);
            return;
        }

        // Получаем ближайший <li>, чтобы точно скрыть/показать таблицу внутри него
        let listItem = recipeTable.closest('li');

        if (!listItem) {
            console.error("List item not found for:", recipeId);
            return;
        }

        // Скрываем все другие таблицы перед открытием новой
        document.querySelectorAll('.recipe-table').forEach(table => {
            if (table !== recipeTable) {
                table.style.display = 'none';
            }
        });

        // Переключаем видимость нужной таблицы
        recipeTable.style.display = (recipeTable.style.display === 'none' || recipeTable.style.display === '') ? 'table' : 'none';
    };
});
