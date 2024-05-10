package com.example.api.model.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EQResults {
    GENERAL_EQ("Общий EQ", """
                Общий эмоциональный показательный коэффициент (EQ). Он показывает, как вы используете эмоции в своей\
                 жизни, и учитывает разные стороны эмоционального интеллекта: отношение к себе и к другим и способности\
                 к общению; отношение к жизни и поиски гармонии."""),

    SELF_AWARENESS("Отношение к себе", """
                Наши эмоции являются выражением наиболее глубинной части нашего существа. Если мы ими пренебрегаем или\
                 гоним их, они все равно вырвутся наружу, но уже в искаженном виде. В этом случае мы будем действовать\
                 под влиянием эмоций, вместо того чтобы осознанно их использовать. Нужно уметь их распознавать и\
                 управлять ими, чтобы отрицательные эмоции не подчинили нас себе."""),

    SOCIAL_INTERACTION("Отношение к другим", """
                Наши эмоции сильно влияют на наши взаимоотношения с людьми: это касается и нашего умения понимать их\
                 (слушать и сочувствовать), и нашей способности к взаимодействию и коммуникации. Некоторые исследователи\
                 полагают, что основная функция эмоций состоит именно в том, чтобы выступать в качестве быстрого и\
                 надежного невербального канала связи. Большинство наших эмоций действительно возникают из наших\
                 социальных связей: чтобы убедиться в этом, всломните, что в последнее время вызывало у вас гнев,\
                 радость, грусть, стыд..."""),

    LIFE_ATTITUDE("Отношение к жизни", """
                То место, которое мы отводим эмоциям в повседневной жизни, несомненно, влияет на наш способ существования\
                 в этом мире. Какие цели мы преследуем? Какие приоритеты мы для себя определили? Каким образом мы заботимся\
                 о себе? Какое место в жизни мы отводим интуиции, творчеству, непосредственности? В этом смысле эмоциональный\
                 интеллект важен как для принятия решений, так и в том, что касается качества нашей жизни.""");

    private final String name;
    private final String description;
}