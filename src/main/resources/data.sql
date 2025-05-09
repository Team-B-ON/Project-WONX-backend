INSERT INTO video (
  id,
  title,
  description,
  rating,
  duration_minutes,
  release_date,
  age_rating,
  background_video_url,
  thumbnail_url
) VALUES (
  RANDOM_UUID(),
  '테스트 콘텐츠',
  '테스트용 설명입니다.',
  4.5,
  110,
  CURRENT_DATE + 3,
  '15+',
  'https://example.com/bg.mp4',
  'https://example.com/thumb.jpg'
);
