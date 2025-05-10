INSERT INTO video (
  id,
  title,
  description,
  rating,
  duration_minutes,
  release_date,
  age_rating,
  background_video_url,
  thumbnail_url,
  view_count,
  tmdb_id,
  genre
) VALUES
  (RANDOM_UUID(), '인사이드 아웃', '감정들의 모험', 4.7, 95, CURRENT_DATE, 'All', 'https://example.com/bg1.mp4', 'https://example.com/thumb1.jpg', 1000, 150540, 'Animation, Family'),
  (RANDOM_UUID(), '겨울왕국', '얼음 여왕의 이야기', 4.8, 102, CURRENT_DATE, 'All', 'https://example.com/bg2.mp4', 'https://example.com/thumb2.jpg', 850, 109445, 'Animation, Adventure'),
  (RANDOM_UUID(), '라푼젤', '탑에 갇힌 공주 이야기', 4.6, 100, CURRENT_DATE, 'All', 'https://example.com/bg3.mp4', 'https://example.com/thumb3.jpg', 920, 38757, 'Animation, Romance'),
  (RANDOM_UUID(), '코코', '죽은 자의 세계 모험', 4.9, 105, CURRENT_DATE, 'All', 'https://example.com/bg4.mp4', 'https://example.com/thumb4.jpg', 880, 354912, 'Animation, Music'),
  (RANDOM_UUID(), '업', '하늘을 나는 집 이야기', 4.5, 96, CURRENT_DATE, 'All', 'https://example.com/bg5.mp4', 'https://example.com/thumb5.jpg', 970, 14160, 'Animation, Comedy');
