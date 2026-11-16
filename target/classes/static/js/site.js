document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('form').forEach(form => {
    form.addEventListener('submit', () => {
      const button = form.querySelector('button[type="submit"]');
      if (button && !button.dataset.noLoading) {
        button.dataset.originalText = button.textContent;
        button.classList.add('is-submitting');
      }
    });
  });
});
