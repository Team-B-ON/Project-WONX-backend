INSERT INTO video (
  id, title, description, rating, duration_minutes,
  release_date, age_rating, background_video_url,
  thumbnail_url, view_count
) VALUES
  (RANDOM_UUID(), '해리 포터', '마법 세계의 소년 이야기', 4.8, 120, CURRENT_DATE, '12+', 'https://example.com/bg1.mp4', 'https://example.com/thumb1.jpg', 500),
  (RANDOM_UUID(), '반지의 제왕', '모험과 우정의 판타지 여정', 4.9, 180, CURRENT_DATE, '15+', 'https://example.com/bg2.mp4', 'https://example.com/thumb2.jpg', 1200),
  (RANDOM_UUID(), '인사이드 아웃', '감정들의 성장 이야기', 4.5, 100, CURRENT_DATE, 'All', 'https://example.com/bg3.mp4', 'https://example.com/thumb3.jpg', 800),
  (RANDOM_UUID(), '겨울왕국', '자신을 찾아가는 엘사의 이야기', 4.7, 110, CURRENT_DATE, 'All', 'https://example.com/bg4.mp4', 'https://example.com/thumb4.jpg', 950);
