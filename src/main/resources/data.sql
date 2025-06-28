-- ✅ GENRES (at least 1 for ManyToMany)
INSERT INTO genres (id, name)
VALUES (1, 'SF') ON DUPLICATE KEY UPDATE name = 'SF';

-- ✅ USERS (for review ownership & personalization)
INSERT INTO users (id, email, nickname, plan_type)
VALUES (
  UUID_TO_BIN('aaaa1111-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
  'user1@wonx.com',
  '유저1',
  'BASIC'
);

-- ✅ MOVIES (includes banner, upcoming, box-office)
INSERT INTO videos (
  id, title, description, rating, duration_minutes, release_date,
  poster_url, age_rating, age_rating_reason, required_plan,
  box_office_rank, view_count
)
VALUES
  -- banner용 (createdAt 가장 최근 가정)
  (
    UUID_TO_BIN('11111111-1111-1111-1111-111111111111'),
    '배너용 영화',
    '이건 배너 테스트용 영화입니다.',
    9.1,
    130,
    '2025-05-27',
    '/poster/banner.jpg',
    '15',
    '폭력성',
    'BASIC',
    1,
    9999
  ),

  -- upcoming (7일 이내 개봉작 총 3개 예시)
  (
    UUID_TO_BIN('22222222-2222-2222-2222-222222222222'),
    '개봉예정 영화1',
    '개봉예정 테스트',
    8.5,
    120,
    '2025-05-30',
    '/poster/upcoming1.jpg',
    '12',
    '공포 요소',
    'BASIC',
    NULL,
    500
  ),
  (
    UUID_TO_BIN('22222222-3333-2222-2222-222222222222'),
    '개봉예정 영화2',
    '개봉예정2',
    8.2,
    110,
    '2025-06-01',
    '/poster/upcoming2.jpg',
    'ALL',
    '가족용',
    'BASIC',
    NULL,
    400
  ),
  (
    UUID_TO_BIN('22222222-4444-2222-2222-222222222222'),
    '개봉예정 영화3',
    '개봉예정3',
    7.9,
    100,
    '2025-06-02',
    '/poster/upcoming3.jpg',
    '12',
    '액션',
    'BASIC',
    NULL,
    300
  ),

  -- box-office (12개 중 상위 3개 예시)
  (
    UUID_TO_BIN('33333333-3333-3333-3333-333333333333'),
    '박스오피스 영화1',
    '조회수 높은 영화1',
    7.2,
    110,
    '2024-04-01',
    '/poster/box1.jpg',
    '15',
    '폭력성',
    'BASIC',
    2,
    88888
  ),
  (
    UUID_TO_BIN('33333333-4444-3333-3333-333333333333'),
    '박스오피스 영화2',
    '조회수 높은 영화2',
    7.0,
    100,
    '2024-04-02',
    '/poster/box2.jpg',
    '15',
    '공포',
    'BASIC',
    3,
    77777
  ),
  (
    UUID_TO_BIN('33333333-5555-3333-3333-333333333333'),
    '박스오피스 영화3',
    '조회수 높은 영화3',
    7.5,
    105,
    '2024-04-03',
    '/poster/box3.jpg',
    '12',
    '누아르',
    'BASIC',
    4,
    66666
  );

-- ✅ VIDEO-GENRE MAPPING (ManyToMany 연결)
INSERT INTO video_genre (video_id, genre_id)
VALUES 
  (UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), 1),
  (UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), 1),
  (UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), 1);

-- ✅ REVIEWS (조회수 높은 순 정렬용, 최소 6개 넣어야 /hot-talks 검증됨)
INSERT INTO reviews (
  id,
  user_id,
  video_id,
  rating,
  content,
  is_anonymous,
  is_deleted,
  created_at,
  view_count
)
VALUES
  (
    UUID_TO_BIN('revw0001-cccc-cccc-cccc-review00001'),
    UUID_TO_BIN('aaaa1111-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    UUID_TO_BIN('33333333-3333-3333-3333-333333333333'),
    9,
    '최고였다제!',
    FALSE,
    FALSE,
    NOW(),
    999
  ),
  (
    UUID_TO_BIN('revw0002-cccc-cccc-cccc-review00002'),
    UUID_TO_BIN('aaaa1111-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    UUID_TO_BIN('33333333-4444-3333-3333-333333333333'),
    8,
    '다시 보고 싶음',
    TRUE,
    FALSE,
    NOW(),
    888
  ),
  (
    UUID_TO_BIN('revw0003-cccc-cccc-cccc-review00003'),
    UUID_TO_BIN('aaaa1111-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    UUID_TO_BIN('33333333-5555-3333-3333-333333333333'),
    7,
    '긴장감 쩔었음',
    FALSE,
    FALSE,
    NOW(),
    777
  ),
  (
    UUID_TO_BIN('revw0004-cccc-cccc-cccc-review00004'),
    UUID_TO_BIN('aaaa1111-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    UUID_TO_BIN('22222222-2222-2222-2222-222222222222'),
    6,
    '예고편만 봤는데 기대됨',
    FALSE,
    FALSE,
    NOW(),
    666
  ),
  (
    UUID_TO_BIN('revw0005-cccc-cccc-cccc-review00005'),
    UUID_TO_BIN('aaaa1111-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    UUID_TO_BIN('22222222-3333-2222-2222-222222222222'),
    10,
    '인생 영화 예약',
    TRUE,
    FALSE,
    NOW(),
    555
  ),
  (
    UUID_TO_BIN('revw0006-cccc-cccc-cccc-review00006'),
    UUID_TO_BIN('aaaa1111-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    UUID_TO_BIN('22222222-4444-2222-2222-222222222222'),
    7,
    'OST 미쳤음',
    FALSE,
    FALSE,
    NOW(),
    444
  );
